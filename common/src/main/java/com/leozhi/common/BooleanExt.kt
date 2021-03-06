package com.leozhi.common

/**
 * @author leozhi
 * @date 21-2-28
 */
sealed class BooleanExt<out T>

class WithData<T>(val data: T): BooleanExt<T>()
object Otherwise: BooleanExt<Nothing>()

inline fun <T> Boolean.isTrue(block: () -> T) = when {
    this -> WithData(block())
    else -> Otherwise
}

inline fun <T> Boolean.isFalse(block: () -> T) = when {
    this -> Otherwise
    else -> WithData(block())
}

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T = when (this) {
    is Otherwise -> block()
    is WithData -> this.data
}

