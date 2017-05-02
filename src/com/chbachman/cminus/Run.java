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
        command("clang " + inputFile + " -o " + outputFile);
    }

    public static void run(String inputFile) {
        command("./" + inputFile);
    }

    public static void buildAndRun(String inputFile, String tempFile) {
        command("clang " + inputFile + " -o " + tempFile, "./" + tempFile);
    }

    public static void command(String... commands) {
        Process current;

        for (int i = 0; i < commands.length; i++) {
            current = command(commands[i]);

            try {
                current.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Process command(String command) {
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
