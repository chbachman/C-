package com.chbachman.cminus.representation.string

import com.chbachman.cminus.Namespace
import com.chbachman.cminus.Variable
import com.chbachman.cminus.representation.CustomType
import com.chbachman.cminus.representation.CustomTypeHeader
import com.chbachman.cminus.representation.Type
import com.chbachman.cminus.representation.function.CustomFunction
import com.chbachman.cminus.representation.function.Function
import com.chbachman.cminus.representation.function.FunctionHeader

private val methods = listOf(
    CustomFunction("size", "return strlen(this)", returnType = Type.Native.Int)
)

class CStringHeader: CustomTypeHeader() {
    override val fullName = "char *"
    override val name = "String"
    override val methods: List<FunctionHeader>
        get() = TODO("not implemented")
    override val properties = emptyList<Variable>()

    override fun getNS(): Namespace {
        TODO("not implemented")
    }

}

class CString: CustomType {
    override val header = CStringHeader()

    override val methods: List<Function>
        get() = TODO("not implemented")
}