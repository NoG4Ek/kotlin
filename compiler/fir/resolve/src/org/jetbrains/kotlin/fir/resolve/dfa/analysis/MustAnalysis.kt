package org.jetbrains.kotlin.fir.resolve.dfa.analysis

import org.jetbrains.kotlin.fir.resolve.dfa.input.IdentityInstruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.Instruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.MoveInstruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.PhiInstruction


class MustAnalysis() {
    private val instrToGraph: MutableMap<Instruction, AliasGraph>
    private var idCounter: Int
    private var lastInstr: Instruction
    val newId: Int
        get() {
            idCounter++
            return idCounter
        }
    val lastGraph: AliasGraph?
        get() = instrToGraph[lastInstr]

    init {
        instrToGraph = HashMap()
        idCounter = 0
        val toInitInstruction: MutableSet<MustAnalysis> = HashSet()
        toInitInstruction.add(this)
        val initInstruction: Instruction = PhiInstruction("-1", toInitInstruction)
        instrToGraph[initInstruction] = AliasGraph()
        lastInstr = initInstruction
    }

    constructor(ma: MustAnalysis) : this() {
        idCounter = ma.idCounter
        instrToGraph.clear()
        for (instr in ma.instrToGraph.keys) {
            instrToGraph[instr] = ma.instrToGraph[instr]!!
        }
        lastInstr = ma.lastInstr
    }

    fun instrToFixpoint(
        instrToPrev: LinkedHashMap<Instruction, Set<Instruction>>,
        instrToNext: LinkedHashMap<Instruction, Set<Instruction>>
    ) {
        val reachableInstr: MutableSet<Instruction> = LinkedHashSet()
        for (instr in instrToPrev.keys) {
            for (instr1 in instrToPrev[instr]!!) {
                reachableInstr.add(instr1)
                instrToGraph[instr1] = AliasGraph()
            }
        }
        for (instr in instrToNext.keys) {
            for (instr1 in instrToNext[instr]!!) {
                reachableInstr.add(instr1)
                instrToGraph[instr1] = AliasGraph()
            }
        }
        applyToFixpoint(instrToPrev, instrToNext, reachableInstr)
    }

    // highly inefficient in many ways, and currently not really up to fixpoint
    //  - replaces the old graphs every time, does not just add deltas
    //  - does not optimize order of instructions to reach fixpoint
    private fun applyToFixpoint(
        instrToPrev: LinkedHashMap<Instruction, Set<Instruction>>,
        instrToNext: LinkedHashMap<Instruction, Set<Instruction>>,
        reachableInstr: Set<Instruction>
    ) {
        var remLastInstr = true
        var instructionsToProcess = reachableInstr
        var loop = 0
        while (instructionsToProcess.isNotEmpty()) {
            val deltaInstructions: MutableSet<Instruction> = LinkedHashSet()
            for (instr in instructionsToProcess) {
                if (instr === lastInstr) break
                var g = instrToGraph[instr]
                val oldGraph = AliasGraph(g!!)
                val nextInstructions: MutableSet<Instruction> = LinkedHashSet()
                val predecessors = instrToPrev[instr]
                if (predecessors != null) {
                    if (predecessors.size == 1) {
                        val curAncestor = predecessors.iterator()
                        val ancestor = curAncestor.next()
                        if (instr === ancestor) {
                            if (lastInstr.id != "-1" && remLastInstr) {
                                remLastInstr = false
                                val predecessorGraph = instrToGraph[lastInstr]
                                g = AliasGraph(predecessorGraph!!)
                            } else {
                                if (lastInstr.id == "-1") {
                                    val predecessorGraph = instrToGraph[lastInstr]
                                    g = AliasGraph(predecessorGraph!!)
                                }
                            }
                        } else {
                            val predecessorGraph = instrToGraph[ancestor]
                            g = AliasGraph(predecessorGraph!!)
                        }
                    } else if (predecessors.size > 1) {
                        val curAncestor = predecessors.iterator()
                        val firstAncestor = curAncestor.next()
                        val secondAncestor = curAncestor.next()
                        var currentGraph = AliasGraph()
                        currentGraph.intersect(instrToGraph[firstAncestor], instrToGraph[secondAncestor])
                        while (curAncestor.hasNext()) {
                            val newGraph = AliasGraph()
                            newGraph.intersect(currentGraph, instrToGraph[curAncestor.next()])
                            currentGraph = newGraph
                        }
                        g = currentGraph
                    }
                }
                val info = applyInstruction(instr, g)
                g = info.second
                var hasChange = info.first
                if (!hasChange) {
                    val areEqual = g == oldGraph
                    hasChange = !areEqual
                }
                if (hasChange) {
                    val nextSet = instrToNext[instr]
                    if (nextSet != null) nextInstructions.addAll(nextSet)
                    deltaInstructions.addAll(nextInstructions)
                }
                lastInstr = instr
            }
            instructionsToProcess = deltaInstructions
            loop++
        }
        println("-- DONE --")
    }

    // Apply instruction semantics on the graph right before the instruction
    // and return whether there was any change
    fun applyInstruction(csinstr: Instruction, graph: AliasGraph): Pair<Boolean, AliasGraph> {
        return when (csinstr.kind) {
            Instruction.Kind.MOVE -> handleMoveInstruction(csinstr, graph)
            Instruction.Kind.IDENTITY -> handleIdentityInstruction(csinstr, graph)
            Instruction.Kind.PHI -> handlePhiInstruction(csinstr, graph)
        }
    }

    private fun handleMoveInstruction(csinstr: Instruction, graph: AliasGraph): Pair<Boolean, AliasGraph> {
        val hGraph = AliasGraph(graph)
        instrToGraph[csinstr] = hGraph
        val instr = csinstr as MoveInstruction
        var right = hGraph.lookupVar(instr.fromVar)
        if (right == null) {
            right = AliasNode()
            right.addVariable(instr.fromVar)
            hGraph.addNode(right)
        }
        val left = hGraph.lookupVar(instr.toVar)
        // from and to are already in the same node
        if (left === right) {
            return Pair(false, hGraph)
        }
        if (left != null) {
            hGraph.removeVariableFromNode(left, instr.toVar)
        }
        hGraph.addVariableToNode(right, instr.toVar)
        hGraph.gcNodes()
        return Pair(true, hGraph)
    }

    private fun handleIdentityInstruction(csinstr: Instruction, graph: AliasGraph): Pair<Boolean, AliasGraph> {
        val hGraph = AliasGraph(graph)
        instrToGraph[csinstr] = hGraph
        val instr = csinstr as IdentityInstruction
        var right = hGraph.lookupVar(instr.fromVar)
        if (right == null) {
            right = AliasNode()
            right.addVariable(instr.fromVar)
            hGraph.addNode(right)
        }
        val left = graph.lookupVar(instr.toVar)
        // from and to are already in the same node
        if (left === right) {
            return Pair(false, hGraph)
        }
        hGraph.addVariableToNode(right, instr.toVar)
        hGraph.gcNodes()
        return Pair(true, hGraph)
    }

    private fun handlePhiInstruction(csinstr: Instruction, graph: AliasGraph): Pair<Boolean, AliasGraph> {
        var hGraph = AliasGraph(graph)
        instrToGraph[csinstr] = hGraph
        val instr = csinstr as PhiInstruction
        val itMust: Iterator<MustAnalysis> = instr.musts.iterator()
        val firstMust = itMust.next()
        var currentGraph = AliasGraph(hGraph)
        if (instr.musts.size > 1) {
            val secondMust = itMust.next()
            currentGraph.intersect(firstMust.instrToGraph[firstMust.lastInstr], secondMust.instrToGraph[secondMust.lastInstr])
            while (itMust.hasNext()) {
                val newGraph = AliasGraph()
                newGraph.intersect(currentGraph, itMust.next().instrToGraph[itMust.next().lastInstr])
                currentGraph = newGraph
            }
        } else {
            return Pair(false, hGraph)
        }
        hGraph = AliasGraph(currentGraph)
        graph.gcNodes()
        return Pair(true, hGraph)
    }

    override fun toString(): String {
        var graphs = ""
        for (instruction in instrToGraph.keys) {
            graphs += """
                ${instruction.toString()}:
                
                """.trimIndent()
            graphs += instrToGraph[instruction].toString()
        }
        return graphs
    }
}