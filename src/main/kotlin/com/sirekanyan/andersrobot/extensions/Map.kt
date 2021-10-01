package com.sirekanyan.andersrobot.extensions

import kotlin.math.absoluteValue

fun <T> Map<Int, T>.getClosest(k: Int): T? {
    var resultKey: Int? = null
    var resultDiff = Int.MAX_VALUE
    for (key in keys.sorted()) {
        val diff = (key - k).absoluteValue
        if (diff <= resultDiff) {
            resultKey = key
            resultDiff = diff
        }
    }
    println("Using $resultKey instead of $k")
    return get(resultKey)
}
