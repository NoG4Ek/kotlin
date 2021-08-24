package org.jetbrains.kotlin.fir.resolve.dfa.input

abstract class Instruction(val id: String, val kind: Kind) :
	Comparable<Instruction>, Cloneable {
	enum class Kind {
		MOVE, PHI, IDENTITY
	}

	override fun compareTo(other: Instruction): Int {
		return id.compareTo(other.id)
	}

	override fun toString(): String {
		return id
	}

	abstract override fun clone(): Any

	override fun equals(other: Any?): Boolean {
		return if (other !is Instruction) false else id == other.id
	}

	override fun hashCode(): Int {
		return id.hashCode()
	}
}