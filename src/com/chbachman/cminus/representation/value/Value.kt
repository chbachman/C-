package com.chbachman.cminus.representation.value

import com.chbachman.cminus.CMinusParser
import com.chbachman.cminus.representation.Scope
import com.chbachman.cminus.representation.Struct
import com.chbachman.cminus.representation.Typed
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.statement.FunctionCall
import com.chbachman.cminus.representation.statement.StructConstructor
import java.util.Optional
import java.util.stream.Collectors

/**
 * Created by Chandler on 4/12/17.
 */
interface Value : Typed {
    fun value(): String
}
