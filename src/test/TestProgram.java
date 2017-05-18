package test;

import com.chbachman.cminus.Run;

import java.io.File;

class TestProgram {

    private static final File testDir = new File("./build/test/");
    private static final String tempFileName = "temp.c";
    private static final String tempFileName2 = "temp2";
    private static final File tempFile = new File(testDir, tempFileName);
    private static final File testFileDir = new File("./src/test/cmSource");

    private static boolean test(File program) {
        try {
            String programName = program.getCanonicalPath();
            String tempName = tempFile.getCanonicalPath();
            String temp2Name = new File(testDir, tempFileName2).getCanonicalPath();

            Run.buildCM(programName, tempName);
            Run.build(tempName, temp2Name).waitFor();

            String c = Run.run(temp2Name);
            String swift = Run.command("swift " + programName);

            return c.equals(swift);
        } catch (Exception e) {
            e.printStackTrace();
            // Compilation failed.
            return false;
        }
    }

    static boolean test(String path) {
        return test(new File(testFileDir, path));
    }

}
