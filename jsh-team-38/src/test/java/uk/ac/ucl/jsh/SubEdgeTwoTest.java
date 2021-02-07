package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class SubEdgeTwoTest {

    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testing_dir = new File("tmp");
        testing_dir.mkdir();

        File testing_file = new File("tmp" + File.separator + "testing_file.txt");
        testing_file.createNewFile();
        FileWriter fileWriter = new FileWriter("tmp" + File.separator + "testing_file.txt", false);
        fileWriter.write("123");
        fileWriter.flush();
        fileWriter.close();

        File testing_file2 = new File("tmp" + File.separator + "testing_file2.txt");
        testing_file2.createNewFile();
        FileWriter fileWriter2 = new FileWriter("tmp" + File.separator + "testing_file2.txt", false);
        fileWriter2.write("456");
        fileWriter2.flush();
        fileWriter2.close();

        GlobalVariable.getInstance().setCurrentDirectory("tmp");
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File dir_to_del = new File("tmp");
        dirDeletionHandler.del_dir(dir_to_del);
    }

    //testing command substitution on edge normal case
    @Test
    public void testJsh_sub_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String output = CommandSubstitution.exec("`echo foo;echo bar`");
        assertTrue(output.endsWith("foo bar"));
        out.close();
    }   
    
    //testing command substitution on edge normal case
    @Test
    public void testJsh_sub_normal2() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Jsh.eval("echo `_echo 1`", out);
        
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    }   
}
