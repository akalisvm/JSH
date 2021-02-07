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

public class SortTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();
        
        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file" + System.lineSeparator() + "line_in_file2");
        testingFileFW.flush();
        testingFileFW.close();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        file_to_del.delete();
    }

    //testing pipe|sort on normal case - correct pipe|sort no arg
    @Test
    public void testJsh_pipe_sort_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Sort().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("line_in_file2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|sort on normal case - correct pipe|sort -r
    @Test
    public void testJsh_pipe_sort_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-r"));
        new Sort().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("line_in_file" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|sort on normal case - correct pipe|sort [FILE]
    @Test
    public void testJsh_pipe_sort_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Sort().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("line_in_file2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|sort on normal case - correct pipe|sort|pipe
    @Test
    public void testJsh_pipe_sort_normal4() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>();
        new Sort().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|sort on normal case - correct pipe|sort -r
    @Test
    public void testJsh_pipe_sort_normal5() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-r"));
        new Sort().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|sort on normal case - correct pipe|sort -r|pwd
    @Test
    public void testJsh_pipe_sort_normal6() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-r"));
        new Sort().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|sort on normal case - sort [FILE]|pwd
    @Test
    public void testJsh_pipe_sort_normal7() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Sort().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
    }
    
    //testing sort on edge exceptional case - pipe|sort -unknown-opt
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_sort_exception1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt"));
        new Sort().exec(args2, null, out2);

        out1.close();
        out2.close();
    }

    //testing pipe on edge exceptional case - pipe|sort extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_sort_exception2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Pwd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-r","testing_file.txt","extra_arg"));
        new Sort().exec(args2, null, out2);

        out1.close();
        out2.close();
    }    
    
    //testing pipe on edge exceptional case - pipe|sort -unknown-opt [FILE]
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_sort_exception3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Head().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt","testing_file.txt"));
        new Sort().exec(args2, null, out2);

        out1.close();
        out2.close();
    }          

    //testing pipe on edge exceptional case - pipe|sort unknown-opt [FILE]
    @Test
    public void testJsh_pipe_sort_exception4() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Pwd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-r","testing_file.txt"));
        new Sort().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("1 2"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("1 2" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }  


    //testing sort on normal case - correct [FILE]
    @Test
    public void testJsh_sort_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Sort().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file2" + System.lineSeparator()));
        out.close();
    }

    //testing sort on normal case - correct -r [FILE]
    @Test
    public void testJsh_sort_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-r","testing_file.txt"));
        new Sort().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file" + System.lineSeparator()));
        out.close();
    }

    //testing sort on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_sort_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Sort().exec(args, null, out);
        out.close();
    }

    //testing sort on exceptional case - -r typed incorrectly
    @Test(expected = RuntimeException.class)
    public void testJsh_sort_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-wrongly_typed","testing_file.txt"));
        new Sort().exec(args, null, out);
        out.close();
    }

    //testing sort on exceptional case - extra args
    @Test(expected = RuntimeException.class)
    public void testJsh_sort_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-r","testing_file.txt","extra_arg"));
        new Sort().exec(args, null, out);
        out.close();
    }

    //testing sort on exceptional case - cannot open incompatible type
    @Test(expected = IOException.class)
    public void testJsh_sort_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-r","testing_pic.jpg"));
        new Sort().exec(args, null, out);
        out.close();
    }

    //testing sort on exceptional case - file not exists
    @Test(expected = RuntimeException.class)
    public void testJsh_sort_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-r","no_such_file"));
        new Sort().exec(args, null, out);
        out.close();
    }
}
