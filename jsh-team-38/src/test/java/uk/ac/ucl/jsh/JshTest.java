package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class JshTest {
    @Before
    public void pipeResetBeforeTest(){
        JshInitializer.reset();
    }

    @After
    public void pipeResetAfterTest(){
        JshInitializer.reset();
    }
    
    //testing pipe|Assign on normal case - correct pipe|Assign
    @Test
    public void testJsh_pipe_assign_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Pwd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("assign assignmentPipeNormalOne=1"));
        new Assign().exec(args2, null, out2);

        assertEquals(out2.toString(), "");
        out1.close();
        out2.close();
    }

    //testing pipe and seq on normal case - correct pipe and seq
    @Test
    public void testJsh_pipeAndSeq_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1;echo 2|cat", out);
        assertEquals(out.toString(), "1" + System.lineSeparator() + "2" + System.lineSeparator());
        out.close();
    }

}




