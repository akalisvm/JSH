package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class SeqTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile2 = new File("tmp" + File.separator + "subfolder" + File.separator + "testFile2.txt");
        testFile2.createNewFile();
        
        FileWriter testFileFW2 = new FileWriter(testFile2.getPath(), false);
        testFileFW2.write("line_in_file_subfolder");
        testFileFW2.flush();
        testFileFW2.close();

        GlobalVariable.getInstance().setCurrentDirectory("tmp");
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File dir_to_del = new File("tmp");
        dirDeletionHandler.del_dir(dir_to_del);
    }

    //testing seq on normal case
    @Test
    public void testJsh_seq_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("ls;pwd", out);
        assertTrue(out.toString().endsWith("tmp" + System.lineSeparator()));
        out.close();
    }

    //testing seq on normal case - nothing entered
    @Test
    public void testJsh_seq_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1;", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    }   

    //testing seq on exceptional case - seq runs properly but app throws exception
    @Test
    public void testJsh_seq_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("ls;head no_such_file;pwd", out);
        assertTrue(out.toString().endsWith(""));
        out.close();
    }

    //testing seq on exceptional case - sytax error
    @Test
    public void testJsh_seq_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1;;echo 2", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    }

    //testing seq on exceptional case - sytax error
    @Test
    public void testJsh_seq_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1; ;echo 2", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    } 
    
    //testing seq on exceptional case - sytax error
    @Test
    public void testJsh_seq_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 1;;;echo 2", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    }

        
    //testing seq on exceptional case - sytax error
    @Test
    public void testJsh_seq_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval(";echo 1", out);
        assertEquals(out.toString(), "");
        out.close();
    }   

    //testing seq on exceptional case - nothing entered
    @Test
    public void testJsh_seq_exception6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval(";;;echo 1", out);
        assertEquals(out.toString(), "");
        out.close();
    }       

    //testing seq on exceptional case - single semicolon
    @Test
    public void testJsh_seq_exception7() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval(";", out);
        assertEquals(out.toString(), "");
        out.close();
    }

}
