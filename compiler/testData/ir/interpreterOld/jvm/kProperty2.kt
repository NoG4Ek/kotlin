// !LANGUAGE: +CompileTimeCalculations

package test

@CompileTimeCalculation
class A(val a: Int) {
    val String.size: Int
        get() = this.length * a
}

const val kproperty2Get = A::class.members.toList()[1].call(A(2), "123").toString()
