package com.chbachman.cminus.representation

interface Expression: Typed, Statement {}

interface Statement {
    val semicolon: Boolean
        get() = true
}