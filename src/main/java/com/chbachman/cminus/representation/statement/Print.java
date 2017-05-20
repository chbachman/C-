package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.gen.CMinusParser;
import com.chbachman.cminus.representation.Parser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Value;

/**
 * Created by Chandler on 4/12/17.
 */
public class Print implements Statement {

    public final Value value;
    public final String modifier;

    public Print(CMinusParser.PrintContext ctx, Scope scope) {
        this.value = Parser.parse(ctx.value(), scope);

        switch (Type.Native.Companion.get(value.getType())) {
            case INT: modifier = "%d"; break;
            case STRING: modifier = "%s"; break;
            case BOOL: modifier = "%s"; break;
            default: throw new RuntimeException("Cannot print value " + value.value());
        }
    }

    @Override
    public String code() {
        if (value.getType() == Type.Native.BOOL.getType()) {
            return "printf(\"" + modifier + "\\n\", " + value.value() + " ? \"true\" : \"false\");";
        }
        return "printf(\"" + modifier + "\\n\", " + value.value() + ");";
    }
}
