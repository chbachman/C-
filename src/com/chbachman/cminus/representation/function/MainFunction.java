package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.representation.value.Variable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Chandler on 4/15/17.
 */
public class MainFunction implements CodeBlock {

    @Override
    public String first() {
        return "int main() {";
    }

    @Override
    public String last() {
        return "    return 0;\n}";
    }
}
