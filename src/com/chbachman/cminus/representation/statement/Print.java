package com.chbachman.cminus.representation.statement;

import com.chbachman.cminus.CMinusParser;
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
        this.value = Value.parse(ctx.value(), scope);

        switch (Type.Native.get(value.type())) {
            case INT: modifier = "%d"; break;
            case STRING: modifier = "%s"; break;
            case BOOL: modifier = "%s"; break;
            default: throw new RuntimeException("Cannot print value " + value.value());
        }
    }

    @Override
    public String code() {
        if (value.type() == Type.Native.BOOL.type) {
            return "printf(\"" + modifier + "\\n\", " + value.value() + " ? \"true\" : \"false\");";
        }
        return "printf(\"" + modifier + "\\n\", " + value.value() + ");";
    }
}
