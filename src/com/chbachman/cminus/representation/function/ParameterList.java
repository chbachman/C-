package com.chbachman.cminus.representation.function;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.value.Variable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 5/2/17.
 */
public class ParameterList {

    public static List<Variable> parse(CMinusParser.ParameterListContext ctx) {
        if (ctx == null) {
            return Collections.emptyList();
        }

        return ctx.parameter().stream().map(p -> new Variable(p)).collect(Collectors.toList());
    }

}
