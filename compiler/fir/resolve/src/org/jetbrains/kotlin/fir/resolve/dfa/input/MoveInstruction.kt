package org.jetbrains.kotlin.fir.resolve.dfa.input

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType

class MoveInstruction(id: String, val toVar: RealVariableAndType, val fromVar: RealVariableAndType) :
    Instruction(id, Kind.MOVE) {
    public override fun clone(): Any {
        return MoveInstruction(id, toVar, fromVar)
    }
}