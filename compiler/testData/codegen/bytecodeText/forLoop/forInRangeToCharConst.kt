const val N = 'Z'

fun test(): Int {
    var sum = 0
    for (i in 'A' .. N) {
        sum += i.toInt()
    }
    return sum
}

// 0 iterator
// 0 getStart
// 0 getEnd
// 0 getFirst
// 0 getLast
// 0 getStep

// JVM_TEMPLATES
// 1 IF_ICMPGT
// 1 IF

// JVM_IR_TEMPLATES
// 1 IF_ICMPGE
// 1 IF