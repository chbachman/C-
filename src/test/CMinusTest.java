package test;

import com.chbachman.cminus.CMinusLexer;
import com.chbachman.cminus.CMinusParser;
import com.chbachman.cminus.Start;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Chandler on 4/15/17.
 */
public class CMinusTest {

    public static String create(String i) {

        CharStream input = CharStreams.fromString(i);
        CMinusLexer lexer = new CMinusLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CMinusParser parser = new CMinusParser(tokens);

        ParseTree tree = parser.init();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        //new Start.PrintOut(tree, ps);
        //String output = new String(baos.toByteArray(), StandardCharsets.UTF_8);

        //System.out.println(output);

        //return output;
        return "";
    }

}
