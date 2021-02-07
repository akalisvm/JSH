package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class EchoTest {
    @Before
    public void pipeResetBeforeTest(){
        JshInitializer.reset();
    }

    @After
    public void pipeResetAfterTest(){
        JshInitializer.reset();
    }

    //testing pipe|echo on normal case - pipe|echo|pipe
    @Test
    public void testJsh_pipe_echo_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("7 8"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "7 8" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }    

        //testing echo on normal case - correct [ARG]
        @Test
        public void testJsh_echo_normal1() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            ArrayList<String> args = new ArrayList<String>(Arrays.asList("foo"));
            new Echo().exec(args, null, out);
            assertEquals(out.toString(), "foo" + System.lineSeparator());
            out.close();
        }
    
        //testing echo on normal case - correct [ARG]
        @Test
        public void testJsh_echo_normal2() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));
            ArrayList<String> args = new ArrayList<String>();
            new Echo().exec(args, null, out);
            assertEquals(out.toString(), " " + System.lineSeparator());
            out.close();
        }
    
}
