package org.jetbrains.kotlin.fir.resolve.dfa.input

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType

class IdentityInstruction(id: String, val toVar: RealVariableAndType, val fromVar: RealVariableAndType) :
    Instruction(id, Kind.IDENTITY) {
    public override fun clone(): Any {
        return IdentityInstruction(id, toVar, fromVar)
    }
}