package com.chbachman.cminus

import com.chbachman.cminus.representation.Statement

object Block {
    var handler: ((Statement) -> Unit)? = null

    fun prepend(s: Statement) {
        handler?.invoke(s)
    }
}