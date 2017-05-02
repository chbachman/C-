package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Variable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/15/17.
 */
public class MainFunction extends CreatedFunction {

    public MainFunction() {
        super(Type.Native.INT.type, "main");
    }

    @Override
    public String last() {
        return "    return 0;\n}";
    }
}
