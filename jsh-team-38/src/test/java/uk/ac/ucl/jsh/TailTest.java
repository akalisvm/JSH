package uk.ac.ucl.jsh;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TailTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp");
        testDir.mkdir();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file");
        testingFileFW.flush();
        testingFileFW.close();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }


    //testing pipe|tail on normal case - correct pipe|tail no arg
    @Test
    public void testJsh_pipe_tail_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Tail().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|tail on normal case - correct pipe|tail correct [FILE]
    @Test
    public void testJsh_pipe_tail_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Tail().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("line_in_file" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|tail on normal case - correct pipe|tail -n number
    @Test
    public void testJsh_pipe_tail_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("line_in_file" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|tail on normal case - correct pipe|tail -n number|pipe
    @Test
    public void testJsh_pipe_tail_normal4() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","1"));
        new Tail().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|tail on normal case - correct pipe|tail|pipe
    @Test
    public void testJsh_pipe_tail_normal5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>();
        new Tail().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|tail on normal case - correct pipe|tail [FILE]|pipe
    @Test
    public void testJsh_pipe_tail_normal6() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Tail().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing tail on edge exceptional case - pipe|tail -unknown-opt number
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_tail_exception7() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1 2","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt","1"));
        new Tail().exec(args2, null, out2);
        out1.close();
        out2.close();
    }

    //testing tail on edge exceptional case - pipe|tail -n not_number
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_tail_exception8() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1 2","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","NOT_int"));
        new Tail().exec(args2, null, out2);
        out1.close();
        out2.close();
    }   

    //testing tail on normal case - correct [OPTION] [FILE]
    @Test
    public void testJsh_tail_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file" + System.lineSeparator()));
        out.close();
    }

    //testing tail on normal case - only [FILE] specified
    @Test
    public void testJsh_tail_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Tail().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file" + System.lineSeparator()));
        out.close();
    }

    //testing tail on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Tail().exec(args, null, out);
        out.close();
    }

    //testing tail on exceptional case - extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt","extra_arg"));
        new Tail().exec(args, null, out);
        out.close();
    }

    //testing tail on exceptional case - -n not specified
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("NOT_n","1","testing_file.txt"));
        new Tail().exec(args, null, out);
        out.close();
    }

    //testing tail on exceptional case - wrong type arg
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","NOT_int","testing_file.txt"));
        new Tail().exec(args, null, out);
        out.close();
    }

    //testing tail on exceptional case - file not exist
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","1","no_such_file"));
        new Tail().exec(args, null, out);
        out.close();
    }

    //testing tail on exceptional case - cannot open incompatible type
    @Test(expected = RuntimeException.class)
    public void testJsh_tail_exception6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp"));
        new Tail().exec(args, null, out);
        out.close();
    }



}
