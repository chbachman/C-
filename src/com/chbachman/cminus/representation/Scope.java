package com.chbachman.cminus.representation;

import com.chbachman.cminus.representation.function.CodeBlock;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.value.Value;
import com.chbachman.cminus.representation.value.Variable;
import com.sun.tools.javac.jvm.Code;

import java.util.*;

/**
 * Created by Chandler on 4/12/17.
 */
public class Scope implements Iterable<Scope.ScopeHolder>{

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
        System.out.println(head.vars);
        head.vars.put(v.name, v);
        System.out.println(head.vars);
    }

    public Optional<Variable> getVariable(String name) {
        for (ScopeHolder scope : this) {
            if (scope.vars.containsKey(name)) {
                return Optional.of(scope.vars.get(name));
            }
        }

        return Optional.empty();
    }

    public void addFunction(Function f) {
        if (f == null) {
            return;
        }

        if (getFunction(f.getBaseName(), f.parameters).isPresent()) {
            throw new RuntimeException("The function " + f.getBaseName() + " already exists.");
        }

        if (head.functions.containsKey(f.getBaseName())) {
            head.functions.get(f.getBaseName()).add(f);
            return;
        }

        List<Function> newList = new ArrayList<>();
        newList.add(f);

        head.functions.put(f.getBaseName(), newList);
    }

    public void addStruct(Struct s) {
        if (s == null) {
            return;
        }

        head.structs.put(s.name, s);
    }

    public List<Struct> getStructs() {
        List<Struct> structList = new ArrayList<>();

        for(ScopeHolder s : this) {
            structList.addAll(s.structs.values());
        }

        return structList;
    }

    public Optional<Struct> getStruct(String name) {
        for (ScopeHolder scope : this) {
            if (scope.structs.containsKey(name)) {
                return Optional.of(scope.structs.get(name));
            }
        }

        return Optional.empty();
    }

    public boolean functionNameExists(String name) {
        for (ScopeHolder scope: this) {
            if (scope.functions.containsKey(name)) {
                return true;
            }
        }

        return false;
    }

    public Optional<Function> getFunction(String name, List<? extends Typed> parameters) {
        for (ScopeHolder scope : this) {
            if (scope.functions.containsKey(name)) {
                List<Function> functions = scope.functions.get(name);

                loop:
                for (Function f : functions) {
                    if (f.parameters.size() != parameters.size()) {
                        continue loop;
                    }

                    for (int i = 0; i < f.parameters.size(); i++) {
                        if (parameters.get(i).type() != f.parameters.get(i).type()) {
                            continue loop;
                        }
                    }

                    return Optional.of(f);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Iterator<ScopeHolder> iterator() {
        return new ScopeIterator(this);
    }

    private class Main implements CodeBlock {
        public Main() {}

        @Override
        public String first() {
            return null;
        }

        @Override
        public String middle() {
            return null;
        }

        @Override
        public String last() {
            return null;
        }
    }

    private class ScopeIterator implements Iterator<ScopeHolder> {

        Optional<ScopeHolder> scope;

        ScopeIterator(Scope s) {
            this.scope = Optional.of(s.head);
        }

        @Override
        public boolean hasNext() {
            return scope.isPresent();
        }

        @Override
        public ScopeHolder next() {
            ScopeHolder s = scope.get();
            scope = s.parent;
            return s;
        }
    }


    class ScopeHolder {
        Map<String, Variable> vars = new TreeMap<>();
        Map<String, List<Function>> functions = new TreeMap<>();
        Map<String, Struct> structs = new TreeMap<>();

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
