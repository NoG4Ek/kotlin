// !LANGUAGE: +NewInference
// !DIAGNOSTICS: -UNUSED_VARIABLE -ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE -UNUSED_VALUE -UNUSED_PARAMETER -UNUSED_EXPRESSION -NOTHING_TO_INLINE
// SKIP_TXT

// FILE: Lib1.kt
package libPackageCase1
import testsCase1.*

public fun <T> emptyArray(): Array<T> = TODO()
fun <T> Case1.emptyArray(): Array<T> = TODO()

// FILE: Lib2.kt
package libPackageCase1Explicit
import testsCase1.*

fun <T> Case1.emptyArray(): Array<T> = TODO()

public fun <T> emptyArray(): Array<T> = TODO()
// FILE: Lib3.kt
package libPackageCase1ExplicitDuplicate
import testsCase1.*

fun <T> Case1.emptyArray(): Array<T> = TODO()

public fun <T> emptyArray(): Array<T> = TODO()

// FILE: LibtestsPack.kt
package testsCase1
fun <T> Case1.emptyArray(): Array<T> = TODO()

public fun <T> emptyArray(): Array<T> = TODO()

// FILE: TestCase.kt
package testsCase1
import libPackageCase1.*
import libPackageCase1Explicit.emptyArray
import libPackageCase1ExplicitDuplicate.emptyArray

// TESTCASE NUMBER: 1
class Case1(){

    fun case1() {
        <!OVERLOAD_RESOLUTION_AMBIGUITY!>emptyArray<!><Int>()
    }
}
