package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.Typed;
import com.chbachman.cminus.representation.value.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/9/17.
 */
public class Function implements Typed, CodeBlock {

    private final Type type;
    public final String name;
    public final List<Variable> parameters;

    public Function(CMinusParser.FuncContext ctx) {
        type = ctx.funcReturn() != null ? Type.from(ctx.funcReturn().type()) : Type.Native.VOID.type;
        name = ctx.ID().getText();

        if (ctx.parameterList() != null) {
            parameters = ctx.parameterList().parameter().stream().map(p -> new Variable(p)).collect(Collectors.toList());
        } else {
            parameters = Collections.EMPTY_LIST;
        }
    }

    protected Function(Type type, String name) {
        this.type = type;
        this.name = name;
        this.parameters = new ArrayList<>();
    }

    public String getHeader() {
        StringBuilder b = new StringBuilder()
                .append(type.code())
                .append(" ")
                .append(name)
                .append("(");

        for (Variable p: parameters) {
            b.append(p.type().code())
                    .append(" ")
                    .append(p.name)
                    .append(", ");
        }

        if (!parameters.isEmpty()) {
            b.delete(b.length() - 2, b.length());
        }

        b.append(");");

        return b.toString();
    }

    @Override
    public String first() {
        StringBuilder b = new StringBuilder().append(getHeader());
        b.deleteCharAt(b.length() - 1).append(" {");

        return b.toString();
    }

    @Override
    public String last() {
        return "}\n";
    }

    @Override
    public Type type() {
        return type;
    }
}
