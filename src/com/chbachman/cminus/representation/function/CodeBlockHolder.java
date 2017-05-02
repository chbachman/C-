package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.statement.Statement;

import java.util.List;

/**
 * Created by Chandler on 5/2/17.
 */
public abstract class CodeBlockHolder implements CodeBlock {

    protected List<Statement> statements;

    public CodeBlockHolder() {}

    public CodeBlockHolder(CMinusParser.CodeBlockContext ctx, Scope scope) {
        init(ctx, scope);
    }

    public CodeBlockHolder(List<Statement> statements) {
        this.statements = statements;
    }

    protected void init(CMinusParser.CodeBlockContext ctx, Scope scope) {
        scope.pushScope(this);
        statements = CodeBlock.parse(ctx, scope);
        scope.popScope();
    }

    @Override
    public String middle() {
        StringBuilder b = new StringBuilder();

        for (Statement s : statements) {
            b.append(s.code());
            b.append('\n');
        }

        return b.toString();
    }
}
