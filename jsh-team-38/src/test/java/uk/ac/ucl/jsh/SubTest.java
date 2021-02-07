package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class SubTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("\"\'\'\"");
        testingFileFW.flush();
        testingFileFW.close();

    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        file_to_del.delete();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo `echo 1`", out);
        assertTrue(out.toString().endsWith("1" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal2() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo `echo 1|echo 2`", out);

        assertTrue(out.toString().endsWith("2" + System.lineSeparator()));
        out.close();
    }


    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal3() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo `echo 1;echo 2`;echo 3", out);

        assertTrue(out.toString().endsWith("3" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal4() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo 1;echo `echo 2;echo 3`", out);

        assertTrue(out.toString().endsWith("3" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal5() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo ';'", out);

        assertTrue(out.toString().endsWith(";" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal6() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo \";\"", out);

        assertTrue(out.toString().endsWith(";" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on echo normal case
    @Test
    public void testJsh_sub_echo_normal7() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo `echo 1 `echo 2;echo 3`;echo 4`;echo 5", out);

        assertTrue(out.toString().endsWith("5" + System.lineSeparator()));
        out.close();
    }

    //testing command substitution on exceptional case - substitution works properly but app throws Exception
    @Test
    public void testJsh_sub_exception1() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo `echo 1`bad_ending", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }
    
    //testing command substitution on edge exceptional case
    @Test
    public void testJsh_sub_exception2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo `echo `echo 1` 2`", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }
    
    //testing command substitution on exceptional case
    @Test(expected = RuntimeException.class)
    public void testJsh_sub_exception3() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo `ls no_such_dir`", out);
        out.close();
    }   

    //testing command substitution on edge case
    @Test
    public void testJsh_edge_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo '|';echo 1", out);
        // (new Seq("echo `echo foo|echo bar`;echo 1", System.out)).accept(new Evaluator());;
        assertTrue(out.toString().endsWith("1" + System.lineSeparator()));
        out.close();
    }   
    
    //testing command substitution on edge case
    @Test
    public void testJsh_edge_normal2() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo `cat testing_file.txt`", out);
        assertTrue(out.toString().endsWith("\"" + System.lineSeparator()));
        out.close();
    }   
}
