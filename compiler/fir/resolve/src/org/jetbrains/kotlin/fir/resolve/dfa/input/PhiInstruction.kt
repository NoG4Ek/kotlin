package org.jetbrains.kotlin.fir.resolve.dfa.input

import org.jetbrains.kotlin.fir.resolve.dfa.analysis.MustAnalysis

class PhiInstruction(id: String, var musts: MutableCollection<MustAnalysis>) :
    Instruction(id, Kind.PHI) {
    fun addFlowMustToPhi(ma: MustAnalysis) {
        musts.add(ma)
    }

    public override fun clone(): Any {
        return PhiInstruction(id, musts)
    }
}