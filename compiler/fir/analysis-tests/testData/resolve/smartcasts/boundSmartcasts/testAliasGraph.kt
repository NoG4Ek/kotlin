// !DUMP_CFG
interface A {
    fun foo()
}

interface B {
    fun bar()
}

fun test_1(x: Any) {
    val y = x
    val z = y
    val c = z
    val o = Any()
    val p = o
    val s = String()
    if (x is A) {
        x.foo()
        y.foo()
        c.foo()
        p.<!UNRESOLVED_REFERENCE!>foo<!>()
    } else {
        if (o is A) {
            x.<!UNRESOLVED_REFERENCE!>foo<!>()
            p.foo()
        }
        if (p !== s) {

        } else o.length
    }
}

fun test_2(a: Any) {
    val x = false
    val b = IntArray(1)

    if (a === x) {
        a.or(true)
    }
    if (a === b) {
        a.iterator()
    }
}

fun test_3() {
    val y = Any()
    val x = y
    if (y is String) {
        x.length
    }
}

fun test_4() {
    val y = Any()
    val x = y
    if (x is String) {
        y.length
    }
}

fun test_5(x: Any, y: Any) {
    val z: Any
    if (true) {
        z = x
    } else {
        z = x
    }

    if (x is B) {
        z.<!UNRESOLVED_REFERENCE!>foo<!>()
        z.bar()
    }
}

fun test_6(x: A?, y: A?) {
    if (y == null) return
    if (x == y) {
        x.foo()
        y.foo()
    }
    if (x !== y) {

    } else {
        x.foo()
        y.foo()
    }
}

fun test_7(x: Any?, y: Any?) {
    if (y == null) return
    if (x !== y) {}
    else {
        if (x is String) {
            y.length
        }
    }
}

open class M(val any: Any?)
class MM(val a: Any?) : M(a) {
    fun mf() {}
}

fun Any.baz() {}

fun test_8(m1: M) {
    val m2 = MM(1)
    val a = m1.any
    a as A
    a.foo() // should be OK
    m1.any.foo() // should be OK
    m1.any.baz() // should be OK

    if (m1 is MM) {
        m1.mf()
    }
    if (m1 === m2) {
       m1.mf()
    }
}

fun inte() = 12
fun test_9(x: Any) {
    val i = inte()

    if (x is String) {
        x.length
    }
    if (x === i) {
        x.inc()
    }
}
