package com.chbachman.cminus.representation.value

import com.chbachman.cminus.representation.Typed

interface Expression: Typed {
    val expression: String
}

interface Statement {
    val statement: String
}
