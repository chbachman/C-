package com.chbachman.cminus.representation;

import com.chbachman.cminus.representation.value.Variable;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by Chandler on 4/12/17.
 */
public class Scope {

    Map<String, Variable> vars = new TreeMap<>();
    Map<String, Type> types = new TreeMap<>();
    Map<String, Function> functions = new TreeMap<>();

    public final CodeBlock block;

    private Optional<Scope> parent;

    public Scope() {
        this(null, new Main());
    }

    public Scope(Scope parent, CodeBlock block) {
        this.parent = Optional.ofNullable(parent);
        this.block = block;
    }

    public void addVariable(Variable v) {
        if (v == null) {
            return;
        }

        vars.put(v.name, v);
    }

    public Optional<Variable> getVariable(String name) {
        if (vars.containsKey(name)) {
            return Optional.of(vars.get(name));
        } else {
            if (parent.isPresent()) {
                return parent.get().getVariable(name);
            } else {
                return Optional.empty();
            }
        }
    }

    public void addFunction(Function f) {
        if (f == null) {
            return;
        }

        functions.put(f.name, f);
    }

    public Optional<Function> getFunction(String name) {
        if (functions.containsKey(name)) {
            return Optional.of(functions.get(name));
        } else {
            if (parent.isPresent()) {
                return parent.get().getFunction(name);
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<Scope> pop() {
        return parent;
    }


}
