package com.chbachman.cminus;

import com.chbachman.cminus.representation.*;
import com.chbachman.cminus.representation.control.Control;
import com.chbachman.cminus.representation.function.CreatedFunction;
import com.chbachman.cminus.representation.function.Function;
import com.chbachman.cminus.representation.function.MainFunction;
import com.chbachman.cminus.representation.statement.Statement;
import com.chbachman.cminus.representation.value.Variable;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.*;

import java.io.*;
import java.util.Optional;

public class Start {

    static PrintStream out;

    static ParseTree generateTree(CharStream input) throws Exception {
        CMinusLexer lexer = new CMinusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CMinusParser parser = new CMinusParser(tokens);
        return parser.init();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new RuntimeException("Need input and output files.");
        }

        File output = new File(args[1]);
        out = new PrintStream(output);
        ParseTree tree = generateTree(CharStreams.fromFileName(args[0]));

        Type.init();
        Scope scope = new Scope();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Pass 1-1: Turn Structs into something more resembling C
        //walker.walk(new Structs(), tree);

        // Pass 1: Grab Function/Struct Headers, create list of structs.
        walker.walk(new Headers(scope), tree);
        out.println();

        // Pass 2: Declare Function Bodies.
        walker.walk(new Functions(scope), tree);

        // Pass 3: Build Main Method at the end.
        walker.walk(new Main(scope), tree);
    }

    public static class Headers extends PrintPass {

        // The current struct to add Variables to while parsing.
        Struct current;

        public Headers(Scope scope) {
            super(scope);
            // Only printing structs, so disable at beginning.
            this.shouldPrint = false;
        }

        @Override
        public void enterFunc(CMinusParser.FuncContext ctx) {
            // Create list of functions defined, and print out the headers.
            // At the beginning of the file, so we can just printout.
            // (This does leave the function headers intermingled with struct declarations.
            // ¯\_(ツ)_/¯
            Function f = new Function(ctx);
            scope.addFunction(f);
            out.println(f.getHeader());
        }

        @Override
        // Setup the struct to recieve variables declared within.
        public void enterStruct(CMinusParser.StructContext ctx) {
            this.shouldPrint = true;
            super.enterStruct(ctx);

            current = new Struct(ctx, scope);
            scope.addStruct(current);
            print(current.first());
            scope.pushScope(current);
        }

        @Override
        // Remove the struct from current, and get rid of the scope.
        public void exitStruct(CMinusParser.StructContext ctx) {
            super.exitStruct(ctx);
            print(scope.popScope().last());
            this.shouldPrint = false;
            current = null;
        }

        @Override
        // Printout the declaration, and save value to create later.
        public void enterVariable(CMinusParser.VariableContext ctx) {
            super.enterVariable(ctx);
            Variable v = new Variable(ctx, scope);
            current.variables.add(v);
            // Convert to only declaration, no inline creation.
            print(new Variable(v.name, v.type()).code());
        }
    }

    public static abstract class PrintPass extends CMinusBaseListener {
        boolean shouldPrint = true;
        Scope scope;

        public PrintPass(Scope scope) {
            this.scope = scope;
        }

        @Override
        public void enterFunc(CMinusParser.FuncContext ctx) {
            super.enterFunc(ctx);

            Optional<Function> f = scope.getFunction(ctx.ID().getText());

            if (!f.isPresent()) {
                throw new RuntimeException("Function " + ctx.ID().getText() + " does not exist");
            }

            print(f.get().first());

            scope.pushScope(f.get());
        }

        @Override
        public void exitFunc(CMinusParser.FuncContext ctx) {
            super.exitFunc(ctx);
            print(scope.popScope().last());
        }

        @Override
        public void enterControl(CMinusParser.ControlContext ctx) {
            super.enterControl(ctx);
            Control c = Control.parse(ctx, scope);
            print(c.first());
            scope.pushScope(c);
        }

        @Override
        public void exitControl(CMinusParser.ControlContext ctx) {
            super.exitControl(ctx);
            print(scope.popScope().last());
        }

        @Override
        public void enterStatement(CMinusParser.StatementContext ctx) {
            super.enterStatement(ctx);
            print(Statement.parse(ctx, scope).code());
        }

        protected void print(String s) {
            if (shouldPrint) {
                out.println(scope.getWhitespace() + s);
            }
        }

        protected void printLines(String s) {
            for (String string : s.split("\n")) {
                print(string);
            }
        }
    }

    public static class Functions extends PrintPass {

        public Functions(Scope scope) {
            super(scope);
            this.shouldPrint = false;
        }

        @Override
        public void enterFunc(CMinusParser.FuncContext ctx) {
            this.shouldPrint = true;
            super.enterFunc(ctx);
        }

        @Override
        public void exitFunc(CMinusParser.FuncContext ctx) {
            super.exitFunc(ctx);
            this.shouldPrint = false;
        }

        @Override
        public void exitInit(CMinusParser.InitContext ctx) {
            super.exitInit(ctx);
            this.shouldPrint = true;

            for (Struct s: scope.getStructs()) {
                CreatedFunction f = s.initFunc(scope);
                print(f.first());
                scope.pushScope(f);
                printLines(f.middle());
                scope.popScope();
                print(f.last());
            }
        }
    }

    public static class Main extends PrintPass {

        public Main(Scope scope) {
            super(scope);
        }

        @Override
        public void enterInit(CMinusParser.InitContext ctx) {
            super.enterInit(ctx);
            MainFunction f = new MainFunction();
            print(f.first());
            scope.pushScope(f);
        }

        @Override
        public void enterStruct(CMinusParser.StructContext ctx) {
            shouldPrint = false;
            super.enterStruct(ctx);
        }

        @Override
        public void exitStruct(CMinusParser.StructContext ctx) {
            shouldPrint = false;
            super.exitStruct(ctx);
        }

        @Override
        public void enterFunc(CMinusParser.FuncContext ctx) {
            shouldPrint = false;
            super.enterFunc(ctx);
        }

        @Override
        public void exitFunc(CMinusParser.FuncContext ctx) {
            super.exitFunc(ctx);
            shouldPrint = true;
        }

        @Override
        public void exitInit(CMinusParser.InitContext ctx) {
            super.exitInit(ctx);
            print(scope.popScope().last());
        }
    }
}


