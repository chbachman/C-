package com.chbachman.cminus;

import com.chbachman.cminus.representation.CodeBlock;
import com.chbachman.cminus.representation.Function;
import com.chbachman.cminus.representation.Scope;
import com.chbachman.cminus.representation.Type;
import com.chbachman.cminus.representation.statement.Statement;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Start {

    public static PrintStream out;

    static ParseTree generateTree(String filename) throws Exception{
        CharStream input = CharStreams.fromFileName(filename);
        CMinusLexer lexer = new CMinusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CMinusParser parser = new CMinusParser(tokens);
        return parser.init();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new RuntimeException("Need input and output files.");
        }

        File temp = new File(args[1]);
        PrintStream printStream = new PrintStream(temp);
        out = printStream;

        ParseTree tree = generateTree(args[0]);

        // To avoid NPE, we have to init this. ¯\_(ツ)_/¯
        Type.init();

        new PrintOut(tree);
    }

    static class PrintOut extends CMinusBaseListener {

        Scope current;

        public PrintOut(ParseTree tree) {
            current = new Scope();
            PreFuncWalker.walkTree(tree, current);

            ParseTreeWalker walker = new ParseTreeWalker();

            walker.walk(this, tree);
        }

        public void popScope() {
            Optional<Scope> parent = current.pop();
            if(parent.isPresent()) {
                current = parent.get();
            }
        }

        public void pushScope(CodeBlock block) {
            Scope newScope = new Scope(current, block);
            current = newScope;
        }

        /*
        @Override
        public void enterFunc(CMinusParser.FuncContext ctx) {
            Optional<Function> f = current.getFunction(ctx.ID().getText());

            if (!f.isPresent()) {
                throw new RuntimeException("Function " + ctx.ID().getText() + " does not exist");
            }

            Function func = f.get();

            pushScope(func);
        }
        */

        @Override
        public void enterStatement(CMinusParser.StatementContext ctx) {
            current.block.addStatement(Statement.parse(ctx, current));
        }

        @Override
        public void exitInit(CMinusParser.InitContext ctx) {
            out.println("#include <stdio.h>");
            out.println();

            out.println("\n// MARK: Pre Declarations");

            out.println("\n // MARK: Function Body");

            out.println("int main() {");

            for (Statement line: current.block.getStatements()) {
                out.println("    " + line.code());
            }

            out.println("    return 0;");
            out.println("}");
        }
    }
}


