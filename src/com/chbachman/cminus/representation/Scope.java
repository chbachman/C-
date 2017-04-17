package com.chbachman.cminus.representation;

import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.value.Variable;
import com.sun.tools.javac.jvm.Code;

import java.util.*;

/**
 * Created by Chandler on 4/12/17.
 */
public class Scope {

    ScopeHolder head = new ScopeHolder();

    public CodeBlock popScope() {
        ScopeHolder current = this.head;
        Optional<ScopeHolder> parent = head.parent;
        if(parent.isPresent()) {
            this.head = parent.get();
        }

        return current.block;
    }

    public void pushScope(CodeBlock block) {
        head = new ScopeHolder(head, block);
        block.setupScope(this);
    }

    public CodeBlock code() {
        return head.block;
    }

    public void addVariable(Variable v) {
        if (v == null) {
            return;
        }

        head.vars.put(v.name, v);
    }

    public Optional<Variable> getVariable(String name) {
        Optional<ScopeHolder> current = Optional.of(head);

        while (current.isPresent()) {
            ScopeHolder scope = current.get();

            if (scope.vars.containsKey(name)) {
                return Optional.of(scope.vars.get(name));
            }

            current = scope.parent;
        }

        return Optional.empty();
    }

    public void addFunction(Function f) {
        if (f == null) {
            return;
        }

        head.functions.put(f.name, f);
    }

    public Optional<Function> getFunction(String name) {
        Optional<ScopeHolder> current = Optional.of(head);

        while (current.isPresent()) {
            ScopeHolder scope = current.get();

            if (scope.functions.containsKey(name)) {
                return Optional.of(scope.functions.get(name));
            }

            current = scope.parent;
        }

        return Optional.empty();
    }

    public String getWhitespace() {
        StringBuilder whitespace = new StringBuilder();
        Optional<ScopeHolder> current = head.parent;

        while (current.isPresent()) {
            ScopeHolder scope = current.get();

            whitespace.append("    ");

            current = scope.parent;
        }

        return whitespace.toString();
    }

    private class Main implements CodeBlock {
        public Main() {}

        @Override
        public String first() {
            return null;
        }

        @Override
        public String last() {
            return null;
        }
    }


    private class ScopeHolder {
        Map<String, Variable> vars = new TreeMap<>();
        Map<String, Function> functions = new TreeMap<>();

        Optional<ScopeHolder> parent;
        final CodeBlock block;

        public ScopeHolder() {
            this(null, new Main());
        }

        public ScopeHolder(ScopeHolder parent, CodeBlock block) {
            this.parent = Optional.ofNullable(parent);
            this.block = block;
        }


    }


}
