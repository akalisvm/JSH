package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
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


public class GrepTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testingFile2 = new File("testing_file2.txt");
        testingFile2.createNewFile();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile2 = new File("tmp" + File.separator + "subfolder" + File.separator + "testFile2.txt");
        testFile2.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file");
        testingFileFW.flush();
        testingFileFW.close();
        
        FileWriter testingFileFW2 = new FileWriter(testingFile2.getPath(), false);
        testingFileFW2.write("line_in_file2");
        testingFileFW2.flush();
        testingFileFW2.close();

        FileWriter testFileFW2 = new FileWriter(testFile2.getPath(), false);
        testFileFW2.write("line_in_file_subfolder");
        testFileFW2.flush();
        testFileFW2.close();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File file_to_del2 = new File("testing_file2.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        file_to_del2.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }


    //testing pipe|grep on normal case - correct pipe|grep PATTERN which can be found
    @Test
    public void testJsh_pipe_grep_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("3"));
        new Grep().exec(args2, null, out2);

        assertEquals(out2.toString(), "3 4" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|grep on normal case - correct pipe|grep PATTERN which cannot be found
    @Test
    public void testJsh_pipe_grep_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("1"));
        new Grep().exec(args2, null, out2);

        assertEquals(out2.toString(), "");
        out1.close();
        out2.close();
    }

    //testing pipe|grep on normal case - correct pipe|grep|pipe
    @Test
    public void testJsh_pipe_grep_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("1"));
        new Grep().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|grep on normal case - correct pipe|grep PATTERN [FILE]|pipe
    @Test
    public void testJsh_pipe_grep_normal4() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("line","testing_file.txt"));
        new Grep().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|grep on normal case - correct pipe|grep PATTERN [FILE]
    @Test
    public void testJsh_pipe_grep_normal5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("line","testing_file.txt"));
        new Grep().exec(args2, null, out2);

        assertEquals(out2.toString(), "line_in_file" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|grep on normal case - correct pipe|grep PATTERN [FILE]
    @Test
    public void testJsh_pipe_grep_normal6() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        
        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("line","testing_file.txt"));
        new Grep().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args2, null, out2);

        assertEquals(out2.toString(), "3 4" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    // //testing pipe|grep on normal case - correct grep|pipe
    // @Test
    // public void testJsh_pipe_grep_normal7() throws Exception{
    //     ByteArrayOutputStream out = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(out));
    //     Jsh.eval("grep line tmp" + File.separator + "subfolder" + File.separator + "*|head -n 3", out);
    //     assertTrue(out.toString().endsWith("line_in_file_subfolder" + System.lineSeparator()));
    //     out.close();
    // }

    //testing grep on normal case - correct [PATTERN] [FILE]
    @Test
    public void testJsh_grep_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("line", "testing_file.txt"));
        new Grep().exec(args, null, out);
        assertEquals(out.toString(), "line_in_file" + System.lineSeparator());
        out.close();
    }

    //testing grep on normal case - correct [PATTERN] [FILE][FILE]
    @Test
    public void testJsh_grep_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("line", "testing_file.txt", "testing_file2.txt"));
        new Grep().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file2" + System.lineSeparator()));
        out.close();
    }

    //testing grep on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_grep_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Grep().exec(args, null, out);
        out.close();
    }

    //testing grep on exceptional case - file not exists
    @Test(expected = RuntimeException.class)
    public void testJsh_grep_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("line", "no_such_file"));
        new Grep().exec(args, null, out);
        out.close();
    }

    //testing grep on exceptional case - is a dir
    @Test(expected = RuntimeException.class)
    public void testJsh_grep_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("line", "tmp"));
        new Grep().exec(args, null, out);
        out.close();
    }

    //testing grep on exceptional case - unreadable file
    @Test(expected = RuntimeException.class)
    public void testJsh_grep_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("line", "testing_pic.jpg"));
        new Grep().exec(args, null, out);
        out.close();
    }

    //testing grep on exceptional case - pipe|grep no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Grep().exec(args2, null, out2);

        out1.close();
        out2.close();
    }    

}
