import kotlin.*
import kotlin.collections.*

@CompileTimeCalculation
fun interface IntPredicate {
    fun accept(i: Int): Boolean

    fun defaultMethod() = 1
}

const val isEven = IntPredicate { it % 2 == 0 }.<!EVALUATED: `false`!>accept(1)<!>
    //.let { predicate -> listOf(1, 2, 3, 4, 5).map { predicate.accept(it) }.joinToString() }
const val callToDefault = IntPredicate { false }.<!EVALUATED: `1`!>defaultMethod()<!>
const val callToString = IntPredicate { false }.<!EVALUATED: `(kotlin.Int) -> kotlin.Boolean`!>toString()<!>
