package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/13/17.
 */
public interface CodeBlock extends Statement {
    String first();

    String last();

    String middle();

    static List<Statement> parse(CMinusParser.CodeBlockContext ctx, Scope scope) {
        if (ctx == null) {
            return Collections.emptyList();
        }

        return ctx.statement().stream().map(s -> Statement.parse(s, scope)).collect(Collectors.toList());
    }

    default void setupScope(Scope scope){}

    default String code() {
        StringBuilder b = new StringBuilder();
        b.append(first());
        b.append(middle());
        b.append(last());
        return b.toString();
    }
}
