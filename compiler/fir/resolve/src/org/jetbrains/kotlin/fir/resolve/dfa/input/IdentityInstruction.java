package org.jetbrains.kotlin.fir.resolve.dfa.input;

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType;

public class IdentityInstruction extends Instruction {
	public final RealVariableAndType toVar;
	public final RealVariableAndType fromVar;

	public IdentityInstruction(String id, RealVariableAndType toVar, RealVariableAndType fromVar) {
		super(id, Kind.IDENTITY);
		this.toVar = toVar;
		this.fromVar = fromVar;
	}

	@Override
	public Object clone() {
		return new IdentityInstruction(id, toVar, fromVar);
	}
}
