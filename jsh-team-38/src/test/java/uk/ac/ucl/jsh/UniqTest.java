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

public class UniqTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp");
        testDir.mkdir();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file" + System.lineSeparator() + "line_in_file" + System.lineSeparator() + "LINE_IN_FILE" + System.lineSeparator() + "NOT_SAME");
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

    //testing pipe|uniq on normal case - no arg
    @Test
    public void testJsh_pipe_uniq_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Uniq().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("NOT_SAME" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|uniq on normal case - pipe|uniq|pipe
    @Test
    public void testJsh_pipe_uniq_normal2() throws Exception{
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
        new Uniq().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|uniq on normal case - -i [FILE]
    @Test
    public void testJsh_pipe_uniq_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-i","testing_file.txt"));
        new Uniq().exec(args2, null, out2);
        
        assertTrue(out2.toString().endsWith("NOT_SAME" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|uniq on normal case - pipe|uniq -i
    @Test
    public void testJsh_pipe_uniq_normal4() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-i"));
        new Uniq().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("NOT_SAME" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|uniq on normal case - pipe|uniq -i|pipe
    @Test
    public void testJsh_pipe_uniq_normal5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-i"));
        new Uniq().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|uniq on normal case - pipe|uniq -i [FILE]|pipe
    @Test
    public void testJsh_pipe_uniq_normal6() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-i","testing_file.txt"));
        new Uniq().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|uniq on normal case - uniq -i [FILE]|pipe
    @Test
    public void testJsh_pipe_uniq_normal7() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-i","testing_file.txt"));
        new Uniq().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing uniq on edge exceptional case - pipe|uniq -unknown-opt
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_uniq_exception1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt"));
        new Uniq().exec(args2, null, out2);
        out1.close();
        out2.close();
    }

    //testing uniq on edge exceptional case - pipe|uniq -i extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_uniq_exception2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-i","testing_file.txt","extra_arg"));
        new Uniq().exec(args2, null, out2);
        out1.close();
        out2.close();
    }

    //testing uniq on edge exceptional case - pipe|uniq -unknown_opt FILE
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_uniq_exception3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt","testing_file.txt"));
        new Uniq().exec(args2, null, out2);
        out1.close();
        out2.close();
    }

    //testing uniq on normal case - correct [FILE]
    @Test
    public void testJsh_uniq_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Uniq().exec(args, null, out);
        assertTrue(out.toString().endsWith("NOT_SAME" + System.lineSeparator()));
        out.close();
    }

    //testing uniq on normal case - correct -i [FILE]
    @Test
    public void testJsh_uniq_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-i","testing_file.txt"));
        new Uniq().exec(args, null, out);
        assertTrue(out.toString().endsWith("NOT_SAME" + System.lineSeparator()));
        out.close();
    } 

    //testing uniq on exceptional case - file not exists
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_file"));
        new Uniq().exec(args, null, out);
        out.close();
    }

    //testing uniq on exceptional case - extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("i","testing_file.txt","extra_arg"));
        new Uniq().exec(args, null, out);
        out.close();
    }

    //testing uniq on exceptional case - -i typed incorrectly
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-unknown_opt","testing_file.txt"));
        new Uniq().exec(args, null, out);
        out.close();
    }

    //testing uniq on exceptional case - cannot open incompatible type
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-i","tmp"));
        new Uniq().exec(args, null, out);
        out.close();
    }

    //testing uniq on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Uniq().exec(args, null, out);
        out.close();
    }
}
