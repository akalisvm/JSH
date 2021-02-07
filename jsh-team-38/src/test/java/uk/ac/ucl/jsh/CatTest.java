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

public class CatTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile = new File("tmp" + File.separator + "testFile.txt");
        testFile.createNewFile();

        File testPic = new File("tmp" + File.separator + "testPic.jpg");
        testPic.createNewFile();

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

    //testing pipe|cat on normal case - correct pipe|cat|pipe with no arg
    @Test
    public void testJsh_pipe_cat_normal1() throws Exception{
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
        new Cat().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith("3 4" + System.lineSeparator()));
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|cat on normal case - correct cat|cat|cat with no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_pipe_cat_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        ByteArrayOutputStream out3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));
        System.setOut(new PrintStream(out3));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>();
        new Cat().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeMid();
        ArrayList<String> args2 = new ArrayList<String>();
        new Cat().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>();
        new Cat().exec(args3, null, out3);

        assertTrue(out3.toString().endsWith(""));
        out1.close();
        out2.close();
        out3.close();
    } 

    //testing pipe|cat on normal case - correct pipe|cat FILE
    @Test
    public void testJsh_pipe_cat_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("1"));
        new Echo().exec(args1, null, out1);
    
        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Cat().exec(args2, null, out2);
    
        assertTrue(out2.toString().endsWith("line_in_file" + System.lineSeparator()));
        out1.close();
        out2.close();
    } 

    //testing cat on normal case - correct [FILE]
    @Test
    public void testJsh_cat_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt"));
        new Cat().exec(args, null, out);
        assertTrue(out.toString().endsWith("line_in_file" + System.lineSeparator()));
        out.close();
    }

    //testing cat on normal case - correct with globbing
    @Test
    public void testJsh_cat_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("cat tmp" + File.separator + "subfolder" + File.separator + "*", out);
        assertTrue(out.toString().endsWith("line_in_file_subfolder" + System.lineSeparator()));
        out.close();
    }

    //testing cat on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_cat_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        assertTrue(args.isEmpty());
        new Cat().exec(args, null, out);
        out.close();
    }

    //testing cat on exceptional case - no such file
    @Test(expected = RuntimeException.class)
    public void testJsh_cat_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_file"));
        new Cat().exec(args, null, out);
        out.close();
    }

    //testing cat on exceptional case - cannot open incompatible type with asterisk
    @Test(expected = RuntimeException.class)
    public void testJsh_cat_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("cat testing_pic*", out);
        out.close();
    }

    //testing cat on exceptional case - cannot open incompatible type with globbing
    @Test(expected = RuntimeException.class)
    public void testJsh_cat_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("cat tmp" + File.separator + "*", out);
        out.close();
    }

    //testing cat on exceptional case - no such dir type with asterisk
    @Test(expected = RuntimeException.class)
    public void testJsh_cat_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("cat no_such_file*", out);
        out.close();
    }

}
