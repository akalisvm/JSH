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


public class WcTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testingFile2 = new File("testing_file2.txt");
        testingFile2.createNewFile();

        File testDir = new File("tmp");
        testDir.mkdir();

        File emptyFolderDir = new File("emptyFolder");
        emptyFolderDir.mkdir();

        File testFile = new File("tmp" + File.separator + "testFile.txt");
        testFile.createNewFile();

        File testFile2 = new File("tmp" + File.separator + "testFile2.txt");
        testFile2.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file");
        testingFileFW.flush();
        testingFileFW.close();

        FileWriter testingFileFW2 = new FileWriter(testingFile2.getPath(), false);
        testingFileFW2.write("line_in_file2");
        testingFileFW2.flush();
        testingFileFW2.close();

        FileWriter testFileFW = new FileWriter(testFile.getPath(), false);
        testFileFW.write("line_in_file_subfolder");
        testFileFW.flush();
        testFileFW.close();

    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File file_to_del2 = new File("testing_file2.txt");
        File dir_to_del = new File("tmp");
        File dir_to_del2 = new File("emptyFolder");
        file_to_del.delete();
        file_to_del2.delete();
        dirDeletionHandler.del_dir(dir_to_del);
        dirDeletionHandler.del_dir(dir_to_del2);
    }

    //testing pipe|wc on normal case - correct wc|pipe
    @Test
    public void testJsh_pipe_wc_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Wc().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","1"));
        new Cut().exec(args2, null, out2);
        assertEquals(out2.toString(), "1" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -l
    @Test
    public void testJsh_pipe_wc_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-l"));
        new Wc().exec(args2, null, out2);
        assertEquals(out2.toString(), "1" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -c
    @Test
    public void testJsh_pipe_wc_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-c"));
        new Wc().exec(args2, null, out2);
        assertEquals(out2.toString(), "4" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -w
    @Test
    public void testJsh_pipe_wc_normal4() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-w"));
        new Wc().exec(args2, null, out2);
        assertEquals(out2.toString(), "1" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct wc pipe|-c|pipe
    @Test
    public void testJsh_pipe_wc_normal5() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-c"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct wc pipe|-l|pipe
    @Test
    public void testJsh_pipe_wc_normal6() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-l"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct wc pipe|-w|pipe
    @Test
    public void testJsh_pipe_wc_normal7() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-w"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc|pipe
    @Test
    public void testJsh_pipe_wc_normal8() throws Exception{
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
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc no arg
    @Test
    public void testJsh_pipe_wc_normal9() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>();
        new Wc().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("4" + System.lineSeparator()));
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -l two file|pipe
    @Test
    public void testJsh_pipe_wc_normal10() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-l","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -c two file|pipe
    @Test
    public void testJsh_pipe_wc_normal11() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-c","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -w two file|pipe
    @Test
    public void testJsh_pipe_wc_normal12() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-w","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc file
    @Test
    public void testJsh_pipe_wc_normal13() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Wc().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
    }    

    //testing pipe|wc on normal case - correct pipe|wc -l file
    @Test
    public void testJsh_pipe_wc_normal14() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-l","testing_file.txt"));
        new Wc().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
    } 

    //testing pipe|wc on normal case - correct pipe|wc -c file
    @Test
    public void testJsh_pipe_wc_normal15() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-c","testing_file.txt"));
        new Wc().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
    }  

    //testing wc on edge exceptional case - pipe|wc -unknown-opt
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_wc_exception1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Pwd().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt"));
        new Wc().exec(args2, null, out2);
        out1.close();
        out2.close();
    }

    //testing pipe|wc on normal case - correct pipe|wc -w file
    @Test
    public void testJsh_pipe_wc_normal16() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("123"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-w","testing_file.txt"));
        new Wc().exec(args2, null, out2);
        assertTrue(out2.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
    }    

    //testing wc on normal case - correct -l [FILE]
    @Test
    public void testJsh_wc_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-l","testing_file.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }

    //testing wc on normal case - correct -w [FILE]
    @Test
    public void testJsh_wc_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-w","testing_file.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }

    //testing wc on normal case - correct -c [FILE]
    @Test
    public void testJsh_wc_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-c","testing_file.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }

    //testing wc on normal case - correct with asterisk
    @Test
    public void testJsh_wc_normal4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("wc" + " " + "tmp" + File.separator + "*", out);
        assertTrue(out.toString().endsWith("total" + System.lineSeparator()));
        out.close();
    }
    
    //testing wc on normal case - correct two files with option -l
    @Test
    public void testJsh_wc_normal5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-l","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("total" + System.lineSeparator()));
        out.close();
    }

    //testing wc on normal case - correct two files with option -c
    @Test
    public void testJsh_wc_normal6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-c","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("total" + System.lineSeparator()));
        out.close();
    }

    //testing wc on normal case - correct two files with option -w
    @Test
    public void testJsh_wc_normal7() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-w","testing_file.txt","testing_file2.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("total" + System.lineSeparator()));
        out.close();
    }    

    //testing wc on normal case - an empty file
    @Test
    public void testJsh_wc_normal8() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file2.txt"));
        new Wc().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file2.txt" + System.lineSeparator()));
        out.close();
    }   

    //testing wc on exceptional case - file not exists
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_file"));
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - wrong number of args with -l
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-l"));
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - wrong number of args with -c
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-c"));
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - wrong number of args with -w
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-w"));
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - unknown option
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-unknown_opt","testing_file.txt"));
        new Wc().exec(args, null, out);
        out.close();
    }

    // //testing wc on exceptional case - no such dir with asterisk
    // @Test(expected = RuntimeException.class)
    // public void testJsh_wc_exception7() throws Exception{
    //     ByteArrayOutputStream out = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(out));
    //     Jsh.eval("wc" + " " + "emptyFolder" + File.separator + "*", out);
    //     out.close();
    // }

    //testing wc on exceptional case - option not starting with '-' and correct [FILE]
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception8() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("c","testing_file.txt"));
        new Wc().exec(args, null, out);
        out.close();
    }

    //testing wc on exceptional case - not a dir
    @Test(expected = RuntimeException.class)
    public void testJsh_wc_exception9() throws Exception{
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    ArrayList<String> args = new ArrayList<String>(Arrays.asList("-c","tmp"));
    new Wc().exec(args, null, out);
    out.close();
    }
}
