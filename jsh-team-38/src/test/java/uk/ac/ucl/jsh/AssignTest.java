package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class AssignTest {
    @Before
    public void pipeResetBeforeTest(){
        JshInitializer.reset();
    }

    @After
    public void pipeResetAfterTest(){
        JshInitializer.reset();
    }

    //testing assign on exceptional case - no value
    @Test(expected = RuntimeException.class)
    public void testJsh_assign_exception1() throws Exception{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("assign assignmentExceptionOne=", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }

    //testing assign on exceptional case - no key
    @Test(expected = RuntimeException.class)
    public void testJsh_assign_exception2() throws Exception{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("assign =2", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }

    //testing assign on exceptional case - no operator
    @Test(expected = RuntimeException.class)
    public void testJsh_assign_exception3() throws Exception{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("assign assignmentExceptionThree3", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }

    //testing assign on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_assign_exception4() throws Exception{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("assign", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }

    //testing assign on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_assign_exception5() throws Exception{
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("assign assignmentExceptionFive", out);
        Scanner scn = new Scanner(in);
        scn.close();
        out.close();
        in.close();
    }


    //testing assign replacement on normal case - $ followed by non-letter (no assignment)
    @Test
    public void testJsh_assignReplace_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo $_", out);
        assertEquals(out.toString(), "$_" + System.lineSeparator());
        out.close();
    }


    //testing assign and its replacement normal case
    @Test
    public void testJsh_assignReplace_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("assign assignmentReplacementNormalOne=a;echo $assignmentReplacementNormalOne$", out);
        assertEquals(out.toString(), "a$" + System.lineSeparator());
        out.close();
    }

    //testing assign replacement on normal case - correct usage with assignment
    @Test
    public void testJsh_assignReplacement_normal2() throws Exception {
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("assign assignReplacementNormalTwo=1", out2);
        Jsh.eval("echo $assignReplacementNormalTwo", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out2.close();
        out.close();
    }

}