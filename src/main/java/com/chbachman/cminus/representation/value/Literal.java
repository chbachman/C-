package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.gen.CMinusParser;
import com.chbachman.cminus.representation.Type;

/**
 * Created by Chandler on 4/12/17.
 * Represents a literal value inside source code.
 */
public class Literal implements Value {

    private Type type;
    private String code;

    public Literal(CMinusParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            type = Type.Native.INT.getType();
            code = ctx.getText();
        } else if (ctx.STRING() != null) {
            type = Type.Native.STRING.getType();
            code = ctx.getText();
        } else if (ctx.BOOL() != null) {
            type = Type.Native.BOOL.getType();
            code = ctx.getText().trim().equals("true") ? "1" : "0";
        }
    }

    @Override
    public Type getType() {
        return type;
    }

    public String value() {
        return code;
    }
}
