package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CdTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp");
        testDir.mkdirs();

    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }
    
    //testing pipe|cd on normal case - cd|cd|cd PATH
    @Test
    public void testJsh_pipe_cd_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("tmp"));
        new Cd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("subfolder"));
        new Cd().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("subfolder_within"));
        new Cd().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("")); 
        out1.close();
        out2.close();
        out3.close();
    }

    //testing cd on normal case - correct PATH
    @Test
    public void testJsh_cd_normal1() throws Exception{        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out2));
        Jsh.eval("cd tmp", out);
        Jsh.eval("ls", out2);
        Jsh.eval("cd ..", out3);
        assertTrue(out2.toString().endsWith(""));
        out.close();
        out2.close();
        out3.close();
    }

    //testing cd on exceptional case - extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_cd_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp","extra_arg"));
        new Cd().exec(args, null, out);
        out.close();
    }

    //testing cd on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_cd_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Cd().exec(args, null, out);
        out.close();
    }

    //testing cd on exceptional case - dir not exists
    @Test(expected = RuntimeException.class)
    public void testJsh_cd_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_dir"));
        new Cd().exec(args, null, out);
        out.close();
    }

    //testing cd on exceptional case - not a dir
    @Test(expected = RuntimeException.class)
    public void testJsh_cd_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Cd().exec(args, null, out);
        out.close();
    }
}
