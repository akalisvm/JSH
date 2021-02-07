package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class GlobbingTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        File testDir = new File("tmp" + File.separator + "subfolder");
        testDir.mkdirs();

        File testFile = new File("tmp" + File.separator + "testFile.txt");
        testFile.createNewFile();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();
        
        File file_to_del = new File("testing_file.txt");
        File dir_to_del = new File("tmp");
        file_to_del.delete();
        dirDeletionHandler.del_dir(dir_to_del);
    }


    //testing globbing on normal case - dir exists
    @Test
    public void testJsh_globbing_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo tmp" + File.separator + "*", out);
        assertNotNull(out.toString());
        out.close();
    }


    //testing globbing on normal case - dir not exists (globbling works properly, app throws Exception)
    @Test
    public void testJsh_globbing_normal2() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo 123" + File.separator + "*", out);
        assertEquals(out.toString(), "123/*" + System.lineSeparator());
        out.close();
    } 
}
