package com.chbachman.cminus.representation;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.value.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/15/17.
 * Handles structs in C-
 * Converts them to C structs and creates methods to allow for initialization.
 */
public class Struct implements CodeBlock, Typed, VariableHolder {

    String name;
    final List<Variable> variables;
    private final List<InitBlock> inits;

    public Struct(CMinusParser.StructContext ctx, Scope scope) {
        this.name = ctx.ID().getText();

        this.variables = ctx.classBlock().variable().stream().map(v -> new Variable(v, scope)).collect(Collectors.toList());
        this.inits = ctx.classBlock().initBlock().stream().map(i -> new InitBlock(i, this, scope)).collect(Collectors.toList());

        // TODO: init / deinit blocks? How will deinit work?
    }

    @NotNull
    public Variable getVariable(String name) {
        for (Variable variable : variables) {
            if (variable.name.equals(name)) {
                return variable;
            }
        }

        return null;
    }

    public Optional<InitBlock> getInit(String name, List<? extends Typed> parameters) {
        name = "init" + name;
        for (InitBlock init: inits) {
            if (init.matches(name, parameters)) {
                return Optional.of(init);
            }
        }

        return Optional.empty();
    }

    @Override
    public String first() {
        return "struct " + name + " {";
    }

    @Override
    public String middle() {
        StringBuilder vars = new StringBuilder();

        for (Variable v : variables) {
            // Convert variable to one without a value.
            vars.append(new Variable(v.name, v.type()).code());
        }

        return vars.toString();
    }

    @Override
    public String last() {
        StringBuilder initFunc = new StringBuilder("};\n");

        for (InitBlock f : inits) {
            initFunc.append(f.code());
        }

        return initFunc.toString();
    }

    @Override
    public Type type() {
        return Type.Companion.from(this);
    }
}
