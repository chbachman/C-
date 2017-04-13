package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.statement.Statement;
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
    public final List<Parameter> parameters;
    public final List<Statement> statements = new ArrayList<>();

    public Function(CMinusParser.FuncContext ctx) {
        type = ctx.funcReturn() != null ? Type.from(ctx.funcReturn().type()) : Type.Native.VOID.type;
        name = ctx.ID().getText();

        if (ctx.parameterList() != null) {
            parameters = ctx.parameterList().parameter().stream().map(p -> new Parameter(p)).collect(Collectors.toList());
        } else {
            parameters = Collections.EMPTY_LIST;
        }
    }

    public String getHeader() {
        StringBuilder b = new StringBuilder()
                .append(type.code())
                .append(" ")
                .append(name)
                .append("(");

        for (Parameter p: parameters) {
            b.append(p.type.code())
                    .append(" ")
                    .append(p.identifier)
                    .append(", ");
        }

        if (!parameters.isEmpty()) {
            b.delete(b.length() - 2, b.length());
        }

        b.append(");");

        return b.toString();
    }

    public String code() {
        // Build first line e.g. int main (int argc, String[] argv) {
        StringBuilder b = new StringBuilder().append(getHeader());
        b.deleteCharAt(b.length() - 1).append(" {\n");

        // Build internal lines
        for (Statement statement : statements) {
            b.append("    ").append(statement.code()).append('\n');
        }

        // Build external lines
        b.append("}\n");

        return b.toString();
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public void addStatement(Statement s) {
        statements.add(s);
    }

    @Override
    public List<Statement> getStatements() {
        return null;
    }

    @Override
    public List<Variable> getVariablesToInit() {
        return null;
    }

    public class Parameter implements Typed {
        public Type type;
        public String identifier;

        public Parameter(CMinusParser.ParameterContext p) {
            this.type = Type.from(p.type());
            this.identifier = p.ID().getText();
        }

        @Override
        public Type type() {
            return type;
        }
    }
}
