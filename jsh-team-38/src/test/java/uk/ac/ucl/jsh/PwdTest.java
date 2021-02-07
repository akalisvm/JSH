package uk.ac.ucl.jsh;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PwdTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile = new File("tmp" + File.separator + "subfolder" + File.separator + "testFile.txt");
        testFile.createNewFile();

        GlobalVariable.getInstance().setCurrentDirectory("tmp");
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File dir_to_del = new File("tmp");
        dirDeletionHandler.del_dir(dir_to_del);
    }

    //testing pipe|pwd on normal case - correct pwd|pwd|pwd
    @Test
    public void testJsh_pipe_pwd_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder");

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Pwd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>();
        new Pwd().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>();
        new Pwd().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("subfolder" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pwd on normal case
    @Test
    public void testJsh_pwd_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Pwd().exec(args, null, out);
        assertTrue(out.toString().endsWith("tmp" + System.lineSeparator()));
        out.close();
    }

    //testing pwd on exceptional case - extra one arg
    @Test(expected = RuntimeException.class)
    public void testJshp_pwd_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("extra_arg"));
        new Pwd().exec(args, null, out);
        out.close();
    }
}
