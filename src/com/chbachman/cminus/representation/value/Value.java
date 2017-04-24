package com.chbachman.cminus.representation.value;

import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Struct;
import com.chbachman.cminus.representation.Typed;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.statement.FunctionCall;
import com.chbachman.cminus.representation.statement.StructConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/12/17.
 */
public interface Value extends Typed {

    static Value parse(CMinusParser.ValueContext ctx, Scope scope) {
        // Regular, non struct variable.
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();
            Optional<Variable> v = scope.getVariable(name);

            if (v.isPresent()) {

                if (ctx.dot() != null) {
                    return parse(ctx.dot(), v.get(), scope);
                }

                return v.get();
            } else {
                throw new RuntimeException("Invalid Variable: " + ctx.ID());
            }
        }

        if (ctx.literal() != null) {
            return new Literal(ctx.literal());
        }

        // Struct Initialization e.g. Struct() is handled with function call.
        if (ctx.functionCall() != null) {
            String name = ctx.functionCall().ID().getText();
            Optional<Struct> struct = scope.getStruct(name);
            Optional<Function> func = scope.getFunction(name);

            if (func.isPresent() && struct.isPresent()) {
                throw new RuntimeException("There is both a struct and function named: " + name);
            }

            if (func.isPresent()) {
                return new FunctionCall(ctx.functionCall(), scope);
            }

            if (struct.isPresent()) {
                return new StructConstructor(ctx.functionCall(), scope);
            }

        }

        if (ctx.op != null) {
            return new Operation(ctx, scope);
        }

        if (ctx.paren != null) {
            return new Paren(ctx, scope);
        }

        throw new RuntimeException("Type of value isn't implemented. " + ctx.getText());
    }

    static Value parse(CMinusParser.DotContext ctx, Variable parent, Scope scope) {
        if (ctx.functionCall() != null) {
            return new FunctionCall(ctx.functionCall(), scope);
        }

        // ITT: Lots of Optional Unwrapping.
        if (ctx.ID() != null) {
            String name = ctx.ID().getText();

            if (!parent.value.isPresent()) {
                throw new RuntimeException("Should never happen, but the struct variable doesn't have a value.");
            }

            if (!(parent.value.get() instanceof StructConstructor)) {
                throw new RuntimeException("Should never happen, but the struct variable isn't a struct.");
            }

            StructConstructor structCon = (StructConstructor) parent.value.get();

            Optional<Variable> variableInStruct = structCon.struct.getVariable(name);

            if (!variableInStruct.isPresent()) {
                throw new RuntimeException("Struct " + parent.type().type + " " + parent.name + " does not contain the variable " + name);
            }

            Variable current = variableInStruct.get();

            if (ctx.dot() != null) {
                Value v = parse(ctx.dot(), current, scope);
                return new Variable(parent.name + "." + v.value(), v);
            }

            return new Variable(parent.name + "." + current.name, current.value.get());
        }

        throw new RuntimeException("Type of dot expression isn't implemented. " + ctx.getText());
    }

    /*
    // Struct Variable
        if (ctx.ID().size() >= 2) {
        List<String> structNames = ctx.ID().stream().map(id -> id.getText()).collect(Collectors.toList());
        Optional<Variable> v = scope.getVariable(structNames.get(0));

        if (!v.isPresent()) {
            throw new RuntimeException("Invalid Variable: " + ctx.ID());
        }

        return getStructVariable(v.get(), 0, structNames);
    }

    static Variable getStructVariable(Variable current, int index, List<String> structNames) {


        System.out.println(structNames);
        System.out.println(index);
        if (structNames.size() - 1 == index) {
            return current;
        }

        String name = structNames.get(index);

        if (!current.value.isPresent()) {
            throw new RuntimeException("Should never happen, but the struct variable doesn't have a value.");
        }

        if (!(current.value.get() instanceof StructConstructor)) {
            throw new RuntimeException("Should never happen, but the struct variable isn't a struct.");
        }

        StructConstructor structCon = (StructConstructor) current.value.get();

        // We finally have the actual struct, after digging through four levels of containment.
        // That's probably a cause of bad design. ¯\_(ツ)_/¯
        Optional<Variable> variableInStruct = structCon.struct.getVariable(structNames.get(index + 1));

        if (!variableInStruct.isPresent()) {
            throw new RuntimeException("Struct " + name + " does not contain the variable " + name);
        }

        Variable v = getStructVariable(variableInStruct.get(), index + 1, structNames);

        return new Variable(current.name + "." + v.name, v.value.get());
    }
    */

    String value();

}
