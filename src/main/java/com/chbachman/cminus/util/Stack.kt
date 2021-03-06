package com.chbachman.cminus.util

/**
 * Created by Chandler on 5/22/17.
 * Adds Stack Methods to Collections
 */

fun <T> MutableList<T>.pop(): T {
    return this.removeAt(0)
}

fun <T> MutableList<T>.popOrNull(): T? {
    if (this.isEmpty()) {
        return null
    }

    return this.removeAt(0)
}

fun <T> List<T>.peek(): T {
    return this[0]
}

fun <T> List<T>.peekOrNull(): T? {
    if (this.isEmpty()) {
        return null
    }

    return this[0]
}

fun <T> MutableList<T>.push(element: T) {
    return this.add(0, element)
}

fun <T> List<T>.toPlainString(): String {
    return this.joinToString("") { it.toString() }
}
