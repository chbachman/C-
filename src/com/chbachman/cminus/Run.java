package com.chbachman.cminus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Chandler on 4/15/17.
 */
public class Run {

    public static void build(String inputFile, String outputFile) {
        runCommand("clang " + inputFile + " -o " + outputFile);
    }

    public static void run(String inputFile) {
        runCommand("./" + inputFile);
    }

    public static void buildAndRun(String inputFile, String tempFile) {
        Process compiler = runCommand("clang " + inputFile + " -o " + tempFile);
        try {
            compiler.waitFor();

            runCommand("./" + tempFile);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Process runCommand(String command) {
        try {
            final Process pr = Runtime.getRuntime().exec(command);

            new Thread(() -> {
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = null;

                try {
                    while ((line = input.readLine()) != null)
                        System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            return pr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
