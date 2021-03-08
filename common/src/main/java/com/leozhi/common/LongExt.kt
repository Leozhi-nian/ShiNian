package com.leozhi.common

/**
 * @author leozhi
 * @date 2021/3/7
 */
fun Long.convert(): String {
    var i = 0
    var num = this.toDouble()
    while (num >= 1024) {
        ++i
        num /= 1024
    }
    val symbol =  when (i) {
        0 -> "B"
        1 -> "K"
        2 -> "M"
        3 -> "G"
        else -> "T"
    }
    return String.format("%.2f $symbol", num)
}