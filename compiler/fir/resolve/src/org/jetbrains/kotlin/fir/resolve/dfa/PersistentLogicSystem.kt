/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.dfa

import com.google.common.collect.ArrayListMultimap
import kotlinx.collections.immutable.*
import org.jetbrains.kotlin.fir.resolve.dfa.analysis.MustAnalysis
import org.jetbrains.kotlin.fir.resolve.dfa.input.IdentityInstruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.Instruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.MoveInstruction
import org.jetbrains.kotlin.fir.resolve.dfa.input.PhiInstruction
import org.jetbrains.kotlin.fir.types.ConeInferenceContext
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.utils.addIfNotNull
import java.util.*

data class PersistentTypeStatement(
    override val variable: RealVariable,
    override val exactType: PersistentSet<ConeKotlinType>,
    override val exactNotType: PersistentSet<ConeKotlinType>
) : TypeStatement() {
    override operator fun plus(other: TypeStatement): PersistentTypeStatement {
        return PersistentTypeStatement(
            variable,
            exactType + other.exactType,
            exactNotType + other.exactNotType
        )
    }

    override val isEmpty: Boolean
        get() = exactType.isEmpty() && exactNotType.isEmpty()

    override fun invert(): PersistentTypeStatement {
        return PersistentTypeStatement(variable, exactNotType, exactType)
    }
}

typealias PersistentApprovedTypeStatements = PersistentMap<RealVariable, PersistentTypeStatement>
typealias PersistentImplications = PersistentMap<DataFlowVariable, PersistentList<Implication>>

class PersistentFlow : Flow {
    val previousFlow: PersistentFlow?
    var approvedTypeStatements: PersistentApprovedTypeStatements
    var logicStatements: PersistentImplications
    val level: Int
    var approvedTypeStatementsDiff: PersistentApprovedTypeStatements = persistentHashMapOf()

    /*
     * val x = a
     * val y = a
     *
     * directAliasMap: { x -> a, y -> a}
     * backwardsAliasMap: { a -> [x, y] }
     */
    override var directAliasMap: PersistentMap<RealVariable, RealVariableAndType>
    override var backwardsAliasMap: PersistentMap<RealVariable, PersistentList<RealVariable>>

    override var mustAnalysis: MustAnalysis

    override var assignmentIndex: PersistentMap<RealVariable, Int>

    constructor(previousFlow: PersistentFlow) {
        this.previousFlow = previousFlow
        approvedTypeStatements = previousFlow.approvedTypeStatements
        logicStatements = previousFlow.logicStatements
        level = previousFlow.level + 1

        mustAnalysis = MustAnalysis(previousFlow.mustAnalysis)
        directAliasMap = previousFlow.directAliasMap
        backwardsAliasMap = previousFlow.backwardsAliasMap
        assignmentIndex = previousFlow.assignmentIndex
    }

    constructor() {
        previousFlow = null
        approvedTypeStatements = persistentHashMapOf()
        logicStatements = persistentHashMapOf()
        level = 1

        mustAnalysis = MustAnalysis()
        directAliasMap = persistentMapOf()
        backwardsAliasMap = persistentMapOf()
        assignmentIndex = persistentMapOf()
    }

    override fun getTypeStatement(variable: RealVariable): TypeStatement? {
        return approvedTypeStatements[variable]
    }

    override fun getImplications(variable: DataFlowVariable): Collection<Implication> {
        return logicStatements[variable] ?: emptyList()
    }

    override fun getVariablesInTypeStatements(): Collection<RealVariable> {
        return approvedTypeStatements.keys
    }

    override fun removeOperations(variable: DataFlowVariable): Collection<Implication> {
        return getImplications(variable).also {
            if (it.isNotEmpty()) {
                logicStatements -= variable
            }
        }
    }
}

abstract class PersistentLogicSystem(context: ConeInferenceContext) : LogicSystem<PersistentFlow>(context) {
    override fun createEmptyFlow(): PersistentFlow {
        return PersistentFlow()
    }

    override fun forkFlow(flow: PersistentFlow): PersistentFlow {
        return PersistentFlow(flow)
    }

    override fun joinFlow(flows: Collection<PersistentFlow>): PersistentFlow {
        return foldFlow(
            flows,
            mergeOperation = { statements -> this.or(statements).takeIf { it.isNotEmpty } },
        )
    }

    override fun unionFlow(flows: Collection<PersistentFlow>): PersistentFlow {
        return foldFlow(
            flows,
            this::and,
        )
    }

    private inline fun foldFlow(
        flows: Collection<PersistentFlow>,
        mergeOperation: (Collection<TypeStatement>) -> MutableTypeStatement?,
    ): PersistentFlow {
        if (flows.isEmpty()) return createEmptyFlow()
        flows.singleOrNull()?.let { return it }

        val aliasedVariablesThatDontChangeAlias = computeAliasesThatDontChange(flows)

        val commonFlow = flows.reduce(::lowestCommonFlow)

        val variables = flows.flatMap { it.approvedTypeStatements.keys }.toSet()
        for (variable in variables) {
            val info = mergeOperation(flows.map { it.getApprovedTypeStatements(variable) }) ?: continue
            removeTypeStatementsAboutVariable(commonFlow, variable)
            val thereWereReassignments = variable.hasDifferentReassignments(flows)
            if (thereWereReassignments) {
                removeLogicStatementsAboutVariable(commonFlow, variable)
                removeAliasInformationAboutVariable(commonFlow, variable)
            }
            commonFlow.addApprovedStatements(info)
        }

        commonFlow.addVariableAliases(aliasedVariablesThatDontChangeAlias)

        val musts = mutableSetOf<MustAnalysis>()
        flows.forEach { musts.add(it.mustAnalysis) }

        val phi = PhiInstruction(commonFlow.mustAnalysis.newId.toString(), musts)

        val toPrev: LinkedHashMap<Instruction, Set<Instruction>> = linkedMapOf(Pair(phi, setOf(phi)))
        commonFlow.mustAnalysis.instrToFixpoint(toPrev, toPrev)
        return commonFlow
    }

    private fun RealVariable.hasDifferentReassignments(flows: Collection<PersistentFlow>): Boolean {
        val firstIndex = flows.first().assignmentIndex[this] ?: -1
        for (flow in flows) {
            val index = flow.assignmentIndex[this] ?: -1
            if (index != firstIndex) return true
        }
        return false
    }

    private fun computeAliasesThatDontChange(
        flows: Collection<PersistentFlow>
    ): MutableMap<RealVariable, RealVariableAndType> {
        val flowsSize = flows.size
        val aliasedVariablesThatDontChangeAlias = mutableMapOf<RealVariable, RealVariableAndType>()

        flows.flatMapTo(mutableSetOf()) { it.directAliasMap.keys }.forEach { aliasedVariable ->
            val originals = flows.map { it.directAliasMap[aliasedVariable] ?: return@forEach }
            if (originals.size != flowsSize) return@forEach
            val firstOriginal = originals.first()
            if (originals.all { it == firstOriginal }) {
                aliasedVariablesThatDontChangeAlias[aliasedVariable] = firstOriginal
            }
        }

        return aliasedVariablesThatDontChangeAlias
    }

    private fun PersistentFlow.addVariableAliases(
        aliasedVariablesThatDontChangeAlias: MutableMap<RealVariable, RealVariableAndType>
    ) {
        for ((alias, underlyingVariable) in aliasedVariablesThatDontChangeAlias) {
            addLocalVariableAlias(this, alias, underlyingVariable)
        }
    }

    private fun PersistentFlow.addApprovedStatements(
        info: MutableTypeStatement
    ) {
        approvedTypeStatements = approvedTypeStatements.addTypeStatement(info)
        if (previousFlow != null) {
            approvedTypeStatementsDiff = approvedTypeStatementsDiff.addTypeStatement(info)
        }
    }

    override fun addLocalVariableAlias(flow: PersistentFlow, alias: RealVariable, underlyingVariable: RealVariableAndType) {
        removeLocalVariableAlias(flow, alias)
        flow.directAliasMap = flow.directAliasMap.put(alias, underlyingVariable)
        flow.backwardsAliasMap = flow.backwardsAliasMap.put(
            underlyingVariable.variable,
            { persistentListOf(alias) },
            { variables -> variables + alias }
        )

        flow.mustAnalysis.lastGraph?.addLinkRVtoRVAT(alias, RealVariableAndType(alias, underlyingVariable.originalType))
        flow.mustAnalysis.lastGraph?.addLinkRVtoRVAT(underlyingVariable.variable, underlyingVariable)
        val mi = MoveInstruction(
            flow.mustAnalysis.newId.toString(),
            flow.mustAnalysis.lastGraph?.getRVATfromRV(alias)!!,
            underlyingVariable
        )
        val toPrev: LinkedHashMap<Instruction, Set<Instruction>> = linkedMapOf(Pair(mi, setOf(mi)))

        flow.mustAnalysis.instrToFixpoint(toPrev, toPrev)
    }

    override fun removeLocalVariableAlias(flow: PersistentFlow, alias: RealVariable) {
        val original = flow.directAliasMap[alias]?.variable ?: return
        flow.directAliasMap = flow.directAliasMap.remove(alias)
        val variables = flow.backwardsAliasMap.getValue(original)
        flow.backwardsAliasMap = flow.backwardsAliasMap.put(original, variables - alias)
        flow.mustAnalysis.lastGraph?.getRVATfromRV(alias)?.let { flow.mustAnalysis.lastGraph?.removeVar(it) }
    }

    @OptIn(DfaInternals::class)
    private fun PersistentFlow.getApprovedTypeStatements(variable: RealVariable): MutableTypeStatement {
        var flow = this
        val result = MutableTypeStatement(variable)
        val variableUnderAlias = directAliasMap[variable]
        if (variableUnderAlias == null) {
            flow.approvedTypeStatements[variable]?.let {
                result += it
            }
        } else {
            result.exactType.addIfNotNull(variableUnderAlias.originalType)
            flow.approvedTypeStatements[variableUnderAlias.variable]?.let { result += it }
        }
        return result
    }

    override fun addTypeStatement(flow: PersistentFlow, statement: TypeStatement) {
        if (statement.isEmpty) return
        with(flow) {
            approvedTypeStatements = approvedTypeStatements.addTypeStatement(statement)
            if (previousFlow != null) {
                approvedTypeStatementsDiff = approvedTypeStatementsDiff.addTypeStatement(statement)
            }
            if (statement.variable.isThisReference) {
                processUpdatedReceiverVariable(flow, statement.variable)
            }
        }
    }

    override fun addImplication(flow: PersistentFlow, implication: Implication) {
        if ((implication.effect as? TypeStatement)?.isEmpty == true) return
        if (implication.condition == implication.effect) return
        with(flow) {
            val variable = implication.condition.variable
            val existingImplications = logicStatements[variable]
            logicStatements = if (existingImplications == null) {
                logicStatements.put(variable, persistentListOf(implication))
            } else {
                logicStatements.put(variable, existingImplications + implication)
            }
        }
    }

    override fun removeTypeStatementsAboutVariable(flow: PersistentFlow, variable: RealVariable) {
        flow.approvedTypeStatements -= variable
        flow.approvedTypeStatementsDiff -= variable
    }

    override fun removeLogicStatementsAboutVariable(flow: PersistentFlow, variable: RealVariable) {
        flow.logicStatements -= variable
        var newLogicStatements = flow.logicStatements
        for ((key, implications) in flow.logicStatements) {
            val implicationsToDelete = mutableListOf<Implication>()
            implications.forEach { implication ->
                if (implication.effect.variable == variable) {
                    implicationsToDelete += implication
                }
            }
            if (implicationsToDelete.isEmpty()) continue
            val newImplications = implications.removeAll(implicationsToDelete)
            newLogicStatements = if (newImplications.isNotEmpty()) {
                newLogicStatements.put(key, newImplications)
            } else {
                newLogicStatements.remove(key)
            }
        }
        flow.logicStatements = newLogicStatements
    }

    override fun removeAliasInformationAboutVariable(flow: PersistentFlow, variable: RealVariable) {
        val existedAlias = flow.directAliasMap[variable]?.variable
        if (existedAlias != null) {
            flow.directAliasMap = flow.directAliasMap.remove(variable)
            val updatedBackwardsAliasList = flow.backwardsAliasMap.getValue(existedAlias).remove(variable)
            flow.backwardsAliasMap = if (updatedBackwardsAliasList.isEmpty()) {
                flow.backwardsAliasMap.remove(existedAlias)
            } else {
                flow.backwardsAliasMap.put(existedAlias, updatedBackwardsAliasList)
            }
        }

        flow.mustAnalysis.lastGraph?.getRVATfromRV(variable)?.let { flow.mustAnalysis.lastGraph?.removeVar(it) }
    }

    override fun translateVariableFromConditionInStatements(
        flow: PersistentFlow,
        originalVariable: DataFlowVariable,
        newVariable: DataFlowVariable,
        shouldRemoveOriginalStatements: Boolean,
        filter: (Implication) -> Boolean,
        transform: (Implication) -> Implication
    ) {
        with(flow) {
            val statements = logicStatements[originalVariable]?.takeIf { it.isNotEmpty() } ?: return
            val newStatements = statements.filter(filter).map {
                val newStatement = OperationStatement(newVariable, it.condition.operation) implies it.effect
                transform(newStatement)
            }.toPersistentList()
            if (shouldRemoveOriginalStatements) {
                logicStatements -= originalVariable
            }
            logicStatements = logicStatements.put(newVariable, logicStatements[newVariable]?.let { it + newStatements } ?: newStatements)
        }
    }

    override fun approveStatementsInsideFlow(
        flow: PersistentFlow,
        approvedStatement: OperationStatement,
        shouldForkFlow: Boolean,
        shouldRemoveSynthetics: Boolean
    ): PersistentFlow {
        val resultFlow = if (shouldForkFlow) forkFlow(flow) else flow

        val approvedFacts = approveOperationStatementsInternal(
            resultFlow,
            approvedStatement,
            initialStatements = null,
            shouldRemoveSynthetics
        )

        if (approvedFacts.isEmpty) return resultFlow

        val updatedReceivers = mutableSetOf<RealVariable>()
        approvedFacts.asMap().forEach { (variable, infos) ->
            var resultInfo = PersistentTypeStatement(variable, persistentSetOf(), persistentSetOf())
            for (info in infos) {
                resultInfo += info
            }
            if (variable.isThisReference) {
                updatedReceivers += variable
            }
            addTypeStatement(resultFlow, resultInfo)
        }

        updatedReceivers.forEach {
            processUpdatedReceiverVariable(resultFlow, it)
        }

        return resultFlow
    }

    private fun approveOperationStatementsInternal(
        flow: PersistentFlow,
        approvedStatement: OperationStatement,
        initialStatements: Collection<Implication>?,
        shouldRemoveSynthetics: Boolean
    ): ArrayListMultimap<RealVariable, TypeStatement> {
        val approvedFacts: ArrayListMultimap<RealVariable, TypeStatement> = ArrayListMultimap.create()
        val approvedStatements = LinkedList<OperationStatement>().apply { this += approvedStatement }
        approveOperationStatementsInternal(flow, approvedStatements, initialStatements, shouldRemoveSynthetics, approvedFacts)
        return approvedFacts
    }

    private fun approveOperationStatementsInternal(
        flow: PersistentFlow,
        approvedStatements: LinkedList<OperationStatement>,
        initialStatements: Collection<Implication>?,
        shouldRemoveSynthetics: Boolean,
        approvedTypeStatements: ArrayListMultimap<RealVariable, TypeStatement>
    ) {
        if (approvedStatements.isEmpty()) return
        val approvedOperationStatements = mutableSetOf<OperationStatement>()
        var firstIteration = true
        while (approvedStatements.isNotEmpty()) {
            @Suppress("NAME_SHADOWING")
            val approvedStatement: OperationStatement = approvedStatements.removeFirst()
            // Defense from cycles in facts
            if (!approvedOperationStatements.add(approvedStatement)) {
                continue
            }
            val statements = initialStatements?.takeIf { firstIteration }
                ?: flow.logicStatements[approvedStatement.variable]?.takeIf { it.isNotEmpty() }
                ?: continue
            if (shouldRemoveSynthetics && approvedStatement.variable.isSynthetic()) {
                flow.previousFlow?.logicStatements = flow.previousFlow?.logicStatements?.minus(approvedStatement.variable)!!
            }
            for (statement in statements) {
                if (statement.condition == approvedStatement) {
                    when (val effect = statement.effect) {
                        is OperationStatement -> approvedStatements += effect
                        is TypeStatement -> approvedTypeStatements.put(effect.variable, effect)
                        is IdentityStatement -> if (effect.isIdentity) {
                            flow.mustAnalysis.lastGraph?.addLinkRVtoRVAT(effect.fromRealVariable.variable, effect.fromRealVariable)
                            flow.mustAnalysis.lastGraph?.addLinkRVtoRVAT(effect.toRealVariable.variable, effect.toRealVariable)
                            val ii = IdentityInstruction(
                                flow.mustAnalysis.newId.toString(),
                                effect.fromRealVariable,
                                effect.toRealVariable
                            )
                            val toPrev: LinkedHashMap<Instruction, Set<Instruction>> = linkedMapOf(Pair(ii, setOf(ii)))

                            flow.mustAnalysis.instrToFixpoint(toPrev, toPrev)
                        }
                    }
                }
            }
            firstIteration = false
        }
    }

    override fun approveStatementsTo(
        destination: MutableTypeStatements,
        flow: PersistentFlow,
        approvedStatement: OperationStatement,
        statements: Collection<Implication>
    ) {
        val approveOperationStatements =
            approveOperationStatementsInternal(flow, approvedStatement, statements, shouldRemoveSynthetics = false)
        approveOperationStatements.asMap().forEach { (variable, infos) ->
            for (info in infos) {
                val mutableInfo = info.asMutableStatement()
                destination.put(variable, mutableInfo) {
                    it += mutableInfo
                    it
                }
            }
        }
    }

    override fun collectInfoForBooleanOperator(
        leftFlow: PersistentFlow,
        leftVariable: DataFlowVariable,
        rightFlow: PersistentFlow,
        rightVariable: DataFlowVariable
    ): InfoForBooleanOperator {
        return InfoForBooleanOperator(
            leftFlow.logicStatements[leftVariable] ?: emptyList(),
            rightFlow.logicStatements[rightVariable] ?: emptyList(),
            rightFlow.approvedTypeStatementsDiff
        )
    }

    override fun getImplicationsWithVariable(flow: PersistentFlow, variable: DataFlowVariable): Collection<Implication> {
        return flow.logicStatements[variable] ?: emptyList()
    }

    override fun recordNewAssignment(flow: PersistentFlow, variable: RealVariable, index: Int) {
        flow.assignmentIndex = flow.assignmentIndex.put(variable, index)
    }

    // --------------------------------------------------------------------\
}

private fun lowestCommonFlow(left: PersistentFlow, right: PersistentFlow): PersistentFlow {
    val level = minOf(left.level, right.level)

    @Suppress("NAME_SHADOWING")
    var left = left
    while (left.level > level) {
        left = left.previousFlow!!
    }
    @Suppress("NAME_SHADOWING")
    var right = right
    while (right.level > level) {
        right = right.previousFlow!!
    }
    while (left != right) {
        left = left.previousFlow!!
        right = right.previousFlow!!
    }
    return left
}

private fun PersistentApprovedTypeStatements.addTypeStatement(info: TypeStatement): PersistentApprovedTypeStatements {
    val variable = info.variable
    val existingInfo = this[variable]
    return if (existingInfo == null) {
        val persistentInfo = if (info is PersistentTypeStatement) info else info.toPersistent()
        put(variable, persistentInfo)
    } else {
        put(variable, existingInfo + info)
    }
}

private fun TypeStatement.toPersistent(): PersistentTypeStatement = PersistentTypeStatement(
    variable,
    exactType.toPersistentSet(),
    exactNotType.toPersistentSet()
)

fun TypeStatement.asMutableStatement(): MutableTypeStatement = when (this) {
    is MutableTypeStatement -> this
    is PersistentTypeStatement -> MutableTypeStatement(variable, exactType.toMutableSet(), exactNotType.toMutableSet())
    else -> throw IllegalArgumentException("Unknown TypeStatement type: ${this::class}")
}
