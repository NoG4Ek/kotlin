// SKIP_INLINE_CHECK_IN: inlineFun$default
// WITH_RUNTIME
// FILE: 1.kt
package test

class A(val value: String) {
    inline fun String.inlineFun(crossinline lambda: () -> String = { { this }() }): String {
        return {
            {
                this + lambda()
            }()
        }()
    }
}

// FILE: 2.kt
// CHECK_CALLED_IN_SCOPE: function=A$inlineFun$lambda scope=box
// CHECK_CALLED_IN_SCOPE: function=A$inlineFun$lambda_0 scope=box
import test.*

fun box(): String {
    val result = with(A("VALUE")) { "OK".inlineFun() }
    return if (result == "OKOK") "OK" else "fail 1: $result"
}
