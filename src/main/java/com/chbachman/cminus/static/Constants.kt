package com.chbachman.cminus.static

object Constants {
    // Not using regular colons since C doesn't support them in variable names. C does support U+A789 though.
    const val COLON_REPLACEMENT = "꞉"
    const val NAMESPACE_REPRESENTATION = COLON_REPLACEMENT + COLON_REPLACEMENT
}

