// !DIAGNOSTICS: -UNUSED_PARAMETER
// NI_EXPECTED_FILE

import kotlin.reflect.KProperty

val a by <!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>a<!>

val b by Delegate(<!DEBUG_INFO_MISSING_UNRESOLVED, TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>b<!>)

val c by <!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE, UNINITIALIZED_VARIABLE!>d<!>
val d by <!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>c<!>

class Delegate(i: Int) {
  operator fun getValue(t: Any?, p: KProperty<*>): Int {
    return 1
  }
}
