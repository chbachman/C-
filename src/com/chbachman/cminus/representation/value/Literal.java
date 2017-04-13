package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Type;

/**
 * Created by Chandler on 4/12/17.
 */
public class Literal implements Value {

    private Type type;
    private String code;

    public Literal(CMinusParser.LiteralContext ctx) {
        if (ctx.INT() != null) {
            type = Type.Native.INT.type;
            code = ctx.getText();
        } else if (ctx.STRING() != null) {
            type = Type.Native.STRING.type;
            code = ctx.getText();
        }
    }

    @Override
    public Type type() {
        return type;
    }

    public String value() {
        return code;
    }
}
