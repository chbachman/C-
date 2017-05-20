package com.chbachman.cminus.representation

import com.chbachman.cminus.representation.value.Variable

/**
 * Created by Chandler on 5/5/17.
 * Used as a way to get vairables from internal sources. Mostly used by Structs and Classes.
 */
interface VariableHolder {

    fun getVariable(name: String): Variable?

}
