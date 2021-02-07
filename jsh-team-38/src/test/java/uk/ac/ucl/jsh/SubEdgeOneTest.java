package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class SubEdgeOneTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testDir = new File("tmp");
        testDir.mkdir();

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
        public void testJsh_sub_edge_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("echo `pwd`", out);
        assertTrue(out.toString().endsWith("tmp" + System.lineSeparator()));
        out.close();

        JshInitializer.reset();
    }
}
