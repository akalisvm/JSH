package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PipeExceptionTest {
    @Before
    public void pipeResetBeforeTest(){
        JshInitializer.reset();
    }

    @After
    public void pipeResetAfterTest(){
        JshInitializer.reset();
    }

    //testing pipe on edge exceptional case - pipe|pipe|pipe
    @Test
    public void testJsh_pipe_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1|echo 2|echo 3", out);
        assertEquals(out.toString(), "3" + System.lineSeparator());
        out.close();
    }

    //testing pipe on edge exceptional case - pipe|
    @Test
    public void testJsh_pipe_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1|", out);
        assertEquals(out.toString(), "" + System.lineSeparator());
        out.close();
    }    

    //testing pipe on edge exceptional case - |
    @Test
    public void testJsh_pipe_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("|", out);
        assertEquals(out.toString(), "" + System.lineSeparator());
        out.close();
    } 
    
    //testing pipe on edge exceptional case - ||
    @Test
    public void testJsh_pipe_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("||", out);
        assertEquals(out.toString(), "" + System.lineSeparator());
        out.close();
    }         

    //testing pipe on edge exceptional case - |pipe
    @Test
    public void testJsh_pipe_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("|pwd", out);
        assertEquals(out.toString(), "" + System.lineSeparator());
        out.close();
    } 

    //testing pipe on exceptional case - pipe||||pipe
    @Test
    public void testJsh_pipe_exception8() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo1||||echo 2", out);
        assertEquals(out.toString(), "" + System.lineSeparator());
        out.close();
    } 
}
