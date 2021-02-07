package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FindTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp" + File.separator + "subfolder" + File.separator + "emptyFolder");
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

        File testDir2 = new File("tmp2");
        testDir2.mkdir();

        File testFileDir2 = new File("tmp2" + File.separator + "testFileDir2.txt");
        testFileDir2.createNewFile();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        File dir_to_del2 = new File("tmp2");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
        dirDeletionHandler.del_dir(dir_to_del2);
    }


    //testing pipe|find on normal case - correct pipe|find -name [FILE]|pipe
    @Test
    public void testJsh_pipe_find_normal1() throws Exception{
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
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-name","testing_file.txt"));
        new Find().exec(args2, null, out2);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args3 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args3, null, out3);

        assertEquals(out3.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
        out3.close();
    }

    //testing pipe|find on normal case - correct pipe|find -name [FILE]
    @Test
    public void testJsh_pipe_find_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-name","testing_file.txt"));
        new Find().exec(args2, null, out2);

        assertTrue(out2.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out1.close();
        out2.close();
    }    

    //testing find on normal case - correct [PATH] -name [FILE]
    @Test
    public void testJsh_find_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-name", "testing_file.txt"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }

    //testing find on normal case - correct [PATH] -name [FILE]
    @Test
    public void testJsh_find_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp", "-name","testFile.txt"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile.txt" + System.lineSeparator()));
        out.close();
    }

    //testing find on normal case - all dir with asterisk
    @Test
    public void testJsh_find_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-name", "testFile*"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile2.txt" + System.lineSeparator()));
        out.close();
    }

    //testing find on normal case
    @Test
    public void testJsh_find_normal4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt","-name", "testing_file.txt"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }    

    //testing find on normal case
    @Test
    public void testJsh_find_normal5() throws Exception{

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    Jsh.eval("find tmp2 -name testFileDir2.txt", out);
    assertTrue(out.toString().endsWith("testFileDir2.txt" + System.lineSeparator()));
    out.close();


        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        // System.setOut(new PrintStream(out));
        // ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp2","-name", "testFileDir2.txt"));
        // new Find().exec(args, null, out);
        // assertTrue(out.toString().endsWith("testFileDir2.txt" + System.lineSeparator()));
        // out.close();
    }

    //testing find on normal case
    @Test
    public void testJsh_find_normal6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("testing_file.txt","-name","testing_file.txt"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    }

    //testing find on normal case - all dir with asterisk
    @Test
    public void testJsh_find_normal7() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-name", "*File2.txt"));
        new Find().exec(args, null, out);
        assertTrue(out.toString().endsWith("testFile2.txt" + System.lineSeparator()));
        out.close();
    }    

    //testing find on exceptional case - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_find_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Find().exec(args, null, out);
        out.close();
    }

    //testing find on exceptional case - wrong number of args
    @Test(expected = RuntimeException.class)
    public void testJsh_find_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-name", "testing_file.txt","extra_arg"));
        new Find().exec(args, null, out);
        out.close();
    }

    //testing find on exceptional case - -name typed incorrectly with 2 args
    @Test(expected = RuntimeException.class)
    public void testJsh_find_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-wrongly_typed","testing_file.txt"));
        new Find().exec(args, null, out);
        out.close();
    }

    //testing find on exceptional case - -name typed incorrectly with 3 args
    @Test(expected = RuntimeException.class)
    public void testJsh_find_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("tmp","-wrongly_typed","testing_file.txt"));
        new Find().exec(args, null, out);
        out.close();
    }

    //testing find on exceptional case - no such dir
    @Test(expected = RuntimeException.class)
    public void testJsh_find_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("no_such_dir","-name","testing_file.txt"));
        new Find().exec(args, null, out);
        out.close();
    }

}
