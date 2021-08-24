/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.resolve.dfa.analysis

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType

class AliasNode {
    private val vars: MutableSet<RealVariableAndType> = HashSet()
    private val labelToNode: MutableMap<RealVariableAndType, AliasNode> = HashMap()
    private val labelFromNode: MutableMap<RealVariableAndType, AliasNode> = HashMap()
    val variables: Set<RealVariableAndType>
        get() = vars
    val outEdgeMap: Map<RealVariableAndType, AliasNode>
        get() = labelToNode
    val inEdgeMap: Map<RealVariableAndType, AliasNode>
        get() = labelFromNode

    fun addVariable(variable: RealVariableAndType) {
        vars.add(variable)
    }

    fun removeVariable(variable: RealVariableAndType) {
        vars.remove(variable)
    }

    fun addOutEdge(label: RealVariableAndType, node: AliasNode) {
        labelToNode[label] = node
        node.labelFromNode[label] = this
    }

    fun removeOutEdge(label: RealVariableAndType) {
        val toNode = labelToNode.remove(label)
        toNode?.labelFromNode?.remove(label)
    }

    fun removeInEdge(label: RealVariableAndType) {
        val fromNode = labelFromNode.remove(label)
        fromNode?.labelToNode?.remove(label)
    }

    override fun toString(): String {
        return "AliasNode(vars=$vars, labelToNode=$labelToNode, labelFromNode=$labelFromNode)"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AliasNode) return false
        return (((vars == other.vars) && (labelToNode == other.labelToNode) && (labelFromNode == other.labelFromNode)))
    }

    override fun hashCode(): Int {
        return (vars.hashCode() + labelToNode.hashCode() + labelFromNode.hashCode()) * 31
    }

    companion object {
        fun intersectVars(n1: AliasNode, n2: AliasNode): AliasNode {
            val newOne = AliasNode()
            val set1IsLarger = n1.vars.size > n2.vars.size
            newOne.vars.addAll(if (set1IsLarger) n2.vars else n1.vars)
            newOne.vars.retainAll(if (set1IsLarger) n1.vars else n2.vars)
            return newOne
        }

        fun cloneVarsOnly(other: AliasNode): AliasNode {
            val newOne = AliasNode()
            newOne.vars.addAll(other.vars)
            return newOne
        }
    }
}