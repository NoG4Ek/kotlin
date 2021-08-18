// Auto-generated by GenerateSteppedRangesCodegenTestData. Do not edit!
// DONT_TARGET_EXACT_BACKEND: WASM
// KJS_WITH_FULL_RUNTIME
// WITH_RUNTIME
import kotlin.test.*

fun two() = 2

fun box(): String {
    val uintList = mutableListOf<UInt>()
    for (i in 8u downTo 1u step two()) {
        uintList += i
    }
    assertEquals(listOf(8u, 6u, 4u, 2u), uintList)

    val ulongList = mutableListOf<ULong>()
    for (i in 8uL downTo 1uL step two().toLong()) {
        ulongList += i
    }
    assertEquals(listOf(8uL, 6uL, 4uL, 2uL), ulongList)

    return "OK"
}