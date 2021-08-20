package org.jetbrains.kotlin.fir.resolve.dfa.analysis;

import java.util.*;

import org.jetbrains.kotlin.fir.resolve.dfa.input.*;

public class MustAnalysis {
	private Set<Instruction> 				reachableInstr;
	private Map<Instruction, AliasGraph> 	instrToGraph;
	private int 								idCounter;
	private Instruction 						lastInstr;

	private LinkedHashMap<Instruction, Set<Instruction>> instrToPrev;
	private LinkedHashMap<Instruction, Set<Instruction>> instrToNext;

	public MustAnalysis() {
		reachableInstr = new TreeSet<>();
		instrToGraph = new HashMap<>();
		idCounter = 0;

		initMustAnalysis();
	}

	public MustAnalysis(MustAnalysis ma) {
		this();

		idCounter = ma.idCounter;
		int ch = 0;
		for (Instruction instr: ma.instrToGraph.keySet()) {
			ch++;
			instrToGraph.put(instr, ma.instrToGraph.get(instr));
			if (ch == ma.instrToGraph.keySet().size()) {
				lastInstr = instr;
			}
		}
	}

	//Using to make linking of RVtoRVAT is possible
	private void initMustAnalysis() {
		Set<MustAnalysis> toInitInstruction = new HashSet<>();
		toInitInstruction.add(this);
		Instruction initInstruction = new PhiInstruction("-1", toInitInstruction);
		instrToGraph.put(initInstruction, new AliasGraph());
		lastInstr = initInstruction;
	}

	public void instrToFixpoint(LinkedHashMap<Instruction, Set<Instruction>> instrToPrev, LinkedHashMap<Instruction, Set<Instruction>> instrToNext) {
		this.instrToPrev = instrToPrev;
		this.instrToNext = instrToNext;

		for (Instruction csinstr : instrToPrev.keySet()) {
			reachableInstr.add(csinstr);
			instrToGraph.put(csinstr, new AliasGraph());
		}
		applyToFixpoint();
		clear();
	}

	private void clear() {
		reachableInstr.clear();
		instrToPrev.clear();
		instrToNext.clear();
	}

	public int getNewId() {
		idCounter++;
		return idCounter;
	}

	public AliasGraph getLastGraph() {
		return instrToGraph.get(lastInstr);
	}

	private Set<Instruction> _newlyReachable;
	private Set<Instruction> _nextInstructions;

	// highly inefficient in many ways, and currently not really up to fixpoint
	//  - replaces the old graphs every time, does not just add deltas
	//  - does not optimize order of instructions to reach fixpoint
	private void applyToFixpoint() {
		boolean remLastInstr = true;
		Set<Instruction> instructionsToProcess = reachableInstr;

		for (int loop = 0; !instructionsToProcess.isEmpty() ; loop++) {
			Set<Instruction> deltaInstructions = new TreeSet<>();
			_newlyReachable = new TreeSet<>();

			for (Instruction csinstr : instructionsToProcess) {
				if (csinstr == lastInstr) break;
				Instruction instr = csinstr;
				AliasGraph g = instrToGraph.get(csinstr);
				AliasGraph oldGraph = new AliasGraph(g);
				_nextInstructions = new TreeSet<>();

					Set<Instruction> predecessors = instrToPrev.get(csinstr);
					if (predecessors != null) {
						if (predecessors.size() == 1) {
							Iterator<Instruction> curAncestor = predecessors.iterator();
							Instruction ancestor = curAncestor.next();
							if (csinstr == ancestor) {
								if (!lastInstr.id.equals("-1") && remLastInstr) {
									remLastInstr = false;
									AliasGraph predecessorGraph = instrToGraph.get(lastInstr);
									g = new AliasGraph(predecessorGraph);
								} else {
									if (lastInstr.id.equals("-1")) {
										AliasGraph predecessorGraph = instrToGraph.get(lastInstr);
										g = new AliasGraph(predecessorGraph);
									}
								}
							} else {
								AliasGraph predecessorGraph = instrToGraph.get(ancestor);
								g = new AliasGraph(predecessorGraph);
							}
						}
						else if (predecessors.size() > 1) {
							Iterator<Instruction> curAncestor = predecessors.iterator();
							Instruction firstAncestor = curAncestor.next();
							Instruction secondAncestor = curAncestor.next();
							AliasGraph currentGraph = new AliasGraph();
							currentGraph.intersect(instrToGraph.get(firstAncestor), instrToGraph.get(secondAncestor));
							while (curAncestor.hasNext()) {
								AliasGraph newGraph = new AliasGraph();
								newGraph.intersect(currentGraph, instrToGraph.get(curAncestor.next()));
								currentGraph = newGraph;
							}
							g = currentGraph;
						}
					}

				boolean hasChange = applyInstruction(csinstr, g);
				//boolean areEqual = g.equals(oldGraph);
				boolean origHasChange = hasChange;
				//if (instr.kind != Instruction.Kind.RESOLVED_CALL && hasChange) {
				//	hasChange = !areEqual;
				//}
				//else if (instr.kind == Instruction.Kind.RESOLVED_CALL) {
				//	hasChange = true;
				//}
				if (!hasChange) {
				//else if (instr.kind != Instruction.Kind.RESOLVED_CALL) {
					boolean areEqual = g.equals(oldGraph);
					hasChange = !areEqual;
				}

				if (hasChange) {
					Set<Instruction> nextSet = instrToNext.get(csinstr);
					if (nextSet != null)
						_nextInstructions.addAll(nextSet);

					deltaInstructions.addAll(_nextInstructions);
				}
				lastInstr = csinstr;
			}
			instructionsToProcess = deltaInstructions;
			reachableInstr.addAll(_newlyReachable);
		}

		System.out.println("-- DONE --");
	}

	// Apply instruction semantics on the graph right before the instruction
	// and return whether there was any change
	boolean applyInstruction(Instruction csinstr, AliasGraph graph) {
		switch (csinstr.kind) {
			case MOVE:
				return handleMoveInstruction(csinstr, graph);
			case IDENTITY:
				return handleIdentityInstruction(csinstr, graph);
			case PHI:
				return handlePhiInstruction(csinstr, graph);
			default:
				//add info message
				return false;
		}
	}

	private boolean handleMoveInstruction(Instruction csinstr, AliasGraph graph) {
		instrToGraph.put(csinstr, graph);
		MoveInstruction instr = (MoveInstruction) csinstr;

		AliasNode right = graph.lookupVar(instr.fromVar);
		if (right == null) {
			right = new AliasNode();
			right.addVariable(instr.fromVar);
			graph.addNode(right);
		}
		AliasNode left = graph.lookupVar(instr.toVar);
		// from and to are already in the same node
		if (left == right) {
			return false;
		}

		if (left != null) {
			graph.removeVariableFromNode(left, instr.toVar);
		}
		graph.addVariableToNode(right, instr.toVar);

		graph.gcNodes();
		return true;
	}

	private boolean handleIdentityInstruction(Instruction csinstr, AliasGraph graph) {
		instrToGraph.put(csinstr, graph);
		IdentityInstruction instr = (IdentityInstruction) csinstr;

		AliasNode right = graph.lookupVar(instr.fromVar);
		if (right == null) {
			right = new AliasNode();
			right.addVariable(instr.fromVar);
			graph.addNode(right);
		}
		AliasNode left = graph.lookupVar(instr.toVar);
		// from and to are already in the same node
		if (left == right) {
			return false;
		}

		graph.addVariableToNode(right, instr.toVar);

		graph.gcNodes();
		return true;
	}

	private boolean handlePhiInstruction(Instruction i, AliasGraph graph) {
		instrToGraph.put(i, graph);
		PhiInstruction instr = (PhiInstruction) i;

		Iterator<MustAnalysis> itMust = instr.musts.iterator();
		MustAnalysis firstMust = itMust.next();
		if (firstMust == null) return false;
		AliasGraph currentGraph = new AliasGraph(graph);

		if (instr.musts.size() > 1) {
			MustAnalysis secondMust = itMust.next();
			currentGraph.intersect(firstMust.instrToGraph.get(firstMust.lastInstr), secondMust.instrToGraph.get(secondMust.lastInstr));
			while (itMust.hasNext()) {
				AliasGraph newGraph = new AliasGraph();
				newGraph.intersect(currentGraph, itMust.next().instrToGraph.get(itMust.next().lastInstr));
				currentGraph = newGraph;
			}
		} else {
			return false;
		}
		graph = new AliasGraph(currentGraph);

		graph.gcNodes();
		return true;
	}

	//@Override
	//public String toString() {
	//	String graphs = "";
	//	for (Instruction instruction: reachableInstr) {
	//		graphs += instruction + ":\n";
	//		graphs += instrToGraph.get(instruction).toString();
	//	}
	//	return graphs;
	//}
}
