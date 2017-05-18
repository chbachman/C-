package com.chbachman.cminus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Chandler on 4/15/17.
 * Runs commands on the command line.
 * Also handles building and running C and C Minus files, for convenience.
 */
public class Run {

    public static void buildCM(String inputFile, String outputFile) {
        new Start(inputFile, outputFile, false);
    }

    public static Process build(String inputFile, String outputFile) {
        return _command("clang " + inputFile + " -o " + outputFile);
    }

    public static String run(String inputFile) {
        return command(inputFile);
    }

    public static void buildAndRun(String inputFile, String tempFile) {
        series("clang " + inputFile + " -o " + tempFile, "./" + tempFile);
    }

    public static void series(String... commands) {
        series(Arrays.asList(commands));
    }

    public static void series(Iterable<String> commands) {
        Process current;

        for (String command: commands) {
            current = _command(command);

            try {
                current.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parallel(String... commands) {
        parallel(Arrays.asList(commands));
    }

    public static void parallel(Iterable<String> commands) {
        for (String command: commands) {
            _command(command);
        }
    }

    public static String command(String command) {
        StringBuilder builder = new StringBuilder();
        try {
            _command(command, builder).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static Process _command(String command, Appendable output) {
        try {
            final Process pr = Runtime.getRuntime().exec(command);

            new Thread(() -> {
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line;

                try {
                    while ((line = input.readLine()) != null){
                        output.append(line);
                        output.append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Process Crashed while running.");
        }
    }

    private static Process _command(String command) {
        return _command(command, System.out);
    }

}
