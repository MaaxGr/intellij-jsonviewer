package com.maaxgr.intellij.jsonviewer.util

class StringReader(private val string: String) {

    var currentPosition = -1

    fun toNext() {
        currentPosition++
    }

    fun toPrevious() {
        currentPosition--
    }

    fun hasNext() = currentPosition + 1 < string.length
    fun hasPrevious() = currentPosition - 1 >= 0

    val current
        get() = string.get(currentPosition)
    val previous
        get() = string.get(currentPosition - 1)
    val next
        get() = string.get(currentPosition + 1)

}
