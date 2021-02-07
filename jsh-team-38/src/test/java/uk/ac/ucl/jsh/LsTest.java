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

public class LsTest {

    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testDir = new File("tmp" + File.separator + "subfolder" + File.separator + "emptyFolder");
        testDir.mkdirs();

        File testDir_exclusive = new File("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder");
        testDir_exclusive.mkdirs();

        File testFile = new File("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder" + File.separator + "testFile.txt");
        testFile.createNewFile();

        GlobalVariable.getInstance().setCurrentDirectory("tmp");
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }

    //testing pipe|ls on normal case - ls|ls|ls with file name
    @Test
    public void testJsh_pipe_ls_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder");

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testFile.txt"));
        new Ls().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testFile.txt"));
        new Ls().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("testFile.txt"));
        new Ls().exec(args3, null, out3);
        assertTrue(out3.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }


    //testing pipe|ls on normal case - ls|ls|ls with path
    @Test
    public void testJsh_pipe_ls_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder");

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("exclusiveFolder"));
        new Ls().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("exclusiveFolder"));
        new Ls().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("exclusiveFolder"));
        new Ls().exec(args3, null, out3);
        assertTrue(out3.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|ls on normal case - ls|ls|ls with no arg
    @Test
    public void testJsh_pipe_ls_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder");

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Ls().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>();
        new Ls().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>();
        new Ls().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    // testing ls on normal case - no arg
    @Test
    public void testJsh_ls_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder");
        new Ls().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out.close();
    }

    //testing ls on normal case - correct [PATH]
    @Test
    public void testJsh_ls_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder");
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("exclusiveFolder"));
        new Ls().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out.close();
    }

    //testing ls on normal case - correct [PATH]
    @Test
    public void testJsh_ls_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder" + File.separator + "exclusiveFolder");
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testFile.txt"));
        new Ls().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out.close();
    }

    //testing ls on exception case - empty folder
    @Test
    public void testJsh_ls_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        GlobalVariable.getInstance().setCurrentDirectory("tmp" + File.separator + "subfolder" + File.separator + "emptyFolder");
        ArrayList<String> args = new ArrayList<String>(Arrays.asList());
        new Ls().exec(args, null, out);
        assertTrue(out.toString().endsWith(""));
        out.close();
    }

    //testing ls on exceptional case - extra one arg
    @Test(expected = RuntimeException.class)
    public void testJsh_ls_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("subfolder","extra_arg"));
        new Ls().exec(args, null, out);
        out.close();
    }


    //testing ls on exceptional case - no_such_dir
    @Test(expected = RuntimeException.class)
    public void testJsh_ls_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_dir"));
        new Ls().exec(args, null, out);
        out.close();
    }
}
