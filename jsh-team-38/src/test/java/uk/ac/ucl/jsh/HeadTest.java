package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

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


public class HeadTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile = new File("tmp" + File.separator + "testFile.txt");
        testFile.createNewFile();

        File testFile2 = new File("tmp" + File.separator + "subfolder" + File.separator + "testFile2.txt");
        testFile2.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file");
        testingFileFW.flush();
        testingFileFW.close();

        FileWriter testFileFW = new FileWriter(testFile.getPath(), false);
        testFileFW.write("line_in_file_tmp");
        testFileFW.flush();
        testFileFW.close();
        
        FileWriter testFileFW2 = new FileWriter(testFile2.getPath(), false);
        testFileFW2.write("line_in_file_subfolder");
        testFileFW2.flush();
        testFileFW2.close();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }
    
    //testing head on normal case - correct [OPTION] [FILE]
    @Test
    public void testJsh_head_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n", "1", "testing_file.txt"));
        new Head().exec(args, null, out);
        assertEquals(out.toString(), "line_in_file" + System.lineSeparator());
        out.close();
    }

    //testing pipe|head on normal case - correct pipe|head no arg
    @Test
    public void testJsh_pipe_head_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Head().exec(args2, null, out2);

        assertEquals(out2.toString(), "3 4" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|head on normal case - correct pipe|head correct [FILE]
    @Test
    public void testJsh_pipe_head_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args2, null, out2);

        assertEquals(out2.toString(), "line_in_file" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|head on normal case - correct pipe|head|pipe
    @Test
    public void testJsh_pipe_head_normal3() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|head on normal case - correct pipe|head|pipe
    @Test
    public void testJsh_pipe_head_normal4() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","1"));
        new Head().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|head on normal case - correct pipe|head -n number
    @Test
    public void testJsh_pipe_head_normal5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","1"));
        new Head().exec(args2, null, out2);

        assertEquals(out2.toString(), "3 4" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing head on normal case - only [FILE] specified
    @Test
    public void testJsh_head_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args, null, out);
        assertEquals(out.toString(), "line_in_file" + System.lineSeparator());
        out.close();
    }

    //testing head on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(""));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on exceptional case - extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt","extra_arg"));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on exceptional case - -n not specified
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("NOT_n","3","testing_file.txt"));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on exceptional case - wrong type arg
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","arg_NOT_int","testing_file.txt"));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on exceptional case - file not exist
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-n","3","no_such_file"));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on exceptional case - cannot open incompatible type
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp"));
        new Head().exec(args, null, out);
        out.close();
    }

    //testing head on edge exceptional case - pipe|head -unknown-opt number
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception7() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknwon_opt","2"));
        new Head().exec(args2, null, out2);

        out1.close();
        out2.close();
    }

    //testing head on edge exceptional case - pipe|head -n not_number
    @Test(expected = RuntimeException.class)
    public void testJsh_head_exception8() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-n","not_number"));
        new Head().exec(args2, null, out2);

        out1.close();
        out2.close();
    }      

}
