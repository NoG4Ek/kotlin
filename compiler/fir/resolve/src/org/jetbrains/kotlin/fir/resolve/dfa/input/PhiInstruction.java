package org.jetbrains.kotlin.fir.resolve.dfa.input;

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType;
import org.jetbrains.kotlin.fir.resolve.dfa.analysis.MustAnalysis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PhiInstruction extends Instruction {
	public Collection<MustAnalysis> musts;

	public PhiInstruction(String id, Collection<MustAnalysis> musts) {
		super(id, Instruction.Kind.PHI);
		this.musts = musts;
	}

	public void addFlowMustToPhi(MustAnalysis ma) {
		this.musts.add(ma);
	}

	@Override
	public Object clone() {
		return new PhiInstruction(id, this.musts);
	}
}
