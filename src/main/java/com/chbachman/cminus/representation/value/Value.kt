package com.chbachman.cminus.representation.value

import com.chbachman.cminus.representation.Typed

/**
 * Created by Chandler on 4/12/17.
 */
interface Value : Typed {
    fun value(): String
}
