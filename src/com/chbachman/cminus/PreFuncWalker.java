package com.chbachman.cminus;

import com.chbachman.cminus.representation.Function;
import com.chbachman.cminus.representation.Scope;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Chandler on 4/9/17.
 */
public class PreFuncWalker extends CMinusBaseListener {

    List<Function> functions = new ArrayList<>();
    Scope scope;

    static List<Function> walkTree(ParseTree tree, Scope s) {
        ParseTreeWalker walker = new ParseTreeWalker();
        PreFuncWalker func = new PreFuncWalker(s);
        walker.walk(func, tree);

        for (Function f : func.functions) {
            s.addFunction(f);
        }

        return func.functions;
    }

    public PreFuncWalker(Scope s) {
        this.scope = s;
    }

    @Override
    public void enterFunc(CMinusParser.FuncContext ctx) {
        functions.add(new Function(ctx));
    }
}
