package com.levopravoce.mobile.common.input

fun String.maxLength(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength)
    } else {
        this
    }
}