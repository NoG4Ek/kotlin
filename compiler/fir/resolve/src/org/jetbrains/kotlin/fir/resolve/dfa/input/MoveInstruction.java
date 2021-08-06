package org.jetbrains.kotlin.fir.resolve.dfa.input;

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariable;
import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType;

public class MoveInstruction extends Instruction {
	public final RealVariableAndType toVar;
	public final RealVariableAndType fromVar;

	public MoveInstruction(String id, RealVariableAndType toVar, RealVariableAndType fromVar) {
		super(id, Instruction.Kind.MOVE);
		this.toVar = toVar;
		this.fromVar = fromVar;
	}

	@Override
	public Object clone() {
		return new MoveInstruction(id, toVar, fromVar);
	}
}
