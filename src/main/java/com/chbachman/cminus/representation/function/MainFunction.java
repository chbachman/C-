package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Type;

/**
 * Created by Chandler on 4/15/17.
 * Handles the creation of the main function with the default template.
 */
public class MainFunction extends CreatedFunction {

    public MainFunction() {
        super(Type.Native.INT.getType(), "main");
    }

    @Override
    public String last() {
        return "    return 0;\n}";
    }
}
