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

public class CutTest {
    @Before
    public void createTemp() throws IOException {
        JshInitializer.reset();

        File testingFile = new File("testing_file.txt");
        testingFile.createNewFile();

        FileWriter testingFileFW = new FileWriter(testingFile.getPath(), false);
        testingFileFW.write("line_in_file");
        testingFileFW.flush();
        testingFileFW.close();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();

        File file_to_del = new File("testing_file.txt");
        file_to_del.delete();
    }

    //testing pipe|cut on normal case - correct pipe|cut -b number
    @Test
    public void testJsh_pipe_cut_normal1() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","1"));
        new Cut().exec(args2, null, out2);

        assertEquals(out2.toString(), "3" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|cut on normal case - correct pipe|cut -b number FILE
    @Test
    public void testJsh_pipe_cut_normal2() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("3 4"));
        new Echo().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","1","testing_file.txt"));
        new Cut().exec(args2, null, out2);

        assertEquals(out2.toString(), "l" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing pipe|cut on normal case - correct cut|pipe
    @Test
    public void testJsh_pipe_cut_normal3() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-b","1","testing_file.txt"));
        new Cut().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("5 6"));
        new Echo().exec(args2, null, out2);

        assertEquals(out2.toString(), "5 6" + System.lineSeparator());
        out1.close();
        out2.close();
    }

    //testing cut on normal case - correct -b OPTION [FILE] with both lower and higher bounds
    @Test
    public void testJsh_cut_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","3-6","testing_file.txt"));
        new Cut().exec(args, null, out);
        assertEquals(out.toString(), "ne_i" + System.lineSeparator());
        out.close();
    }

    //testing cut on normal case - correct -b OPTION [FILE] with lower bound
    @Test
    public void testJsh_cut_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","4-","testing_file.txt"));
        new Cut().exec(args, null, out);
        assertEquals(out.toString(), "e_in_file" + System.lineSeparator());
        out.close();
    }

    //testing Grep on normal case - correct -b OPTION [FILE] with higher bound
    @Test
    public void testJsh_cut_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","-4","testing_file.txt"));
        new Cut().exec(args, null, out);
        assertEquals(out.toString(), "line" + System.lineSeparator());
        out.close();
    }

    //testing cut on normal case - correct -b number [FILE]
    @Test
    public void testJsh_cut_normal4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","4","testing_file.txt"));
        new Cut().exec(args, null, out);
        assertEquals(out.toString(), "e" + System.lineSeparator());
        out.close();
    }

    //testing cut on normal case - correct -b OPTION [FILE] with comma as seperator
    @Test
    public void testJsh_cut_normal5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","1,2","testing_file.txt"));
        new Cut().exec(args, null, out);
        assertEquals(out.toString(), "li" + System.lineSeparator());
        out.close();
    }

    //testing cut on exceptional case - wrong number of args
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>();
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - unknown options
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-unknow_opt","4","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - wrong formmat of number, starting with ','
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b",",4","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - wrong formmat of number, ending with ','
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","4,","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - wrong formmat of number, starting with 0
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","4,","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - wrong formmat of number, not a digit, not ',', not '-'
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception6() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","a","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - cannot open incompatible type
    @Test(expected = IOException.class)
    public void testJsh_cut_exception7() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","4","testing_pic.jpg"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - no such file
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception8() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","2","no_such_file"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - -b only with '-'
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception9() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","-","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }

    //testing cut on exceptional case - -b, starting with '-' and with invalid following
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception10() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","-a","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }
    
    //testing cut on exceptional case - -b with invalid following
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception11() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("-b","--","testing_file.txt"));
        new Cut().exec(args, null, out);
        out.close();
    }    


    //testing cut on edge exceptional case - pipe|cut -unknown-opt number
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception12() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-unknown_opt","2"));
        new Cut().exec(args2, null, out2);

        out1.close();
        out2.close();
    }

    //testing cut on edge exceptional case - pipe|cut -b starting with ','
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception13() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b",",2"));
        new Cut().exec(args2, null, out2);

        out1.close();
        out2.close();
    } 
    
    //testing cut on edge exceptional case - pipe|cut -b ending with ','
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception14() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","2,"));
        new Cut().exec(args2, null, out2);

        out1.close();
        out2.close();
    }
    
    //testing cut on edge exceptional case - pipe|cut -b with index 0
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception15() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","0-9"));
        new Cut().exec(args2, null, out2);

        out1.close();
        out2.close();
    } 
    
    //testing cut on edge exceptional case - pipe|cut -b invalid pattern
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_exception16() throws Exception{
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        System.setOut(new PrintStream(out2));

        PipeSetter.setCmdAtPipeHead();
        ArrayList<String> args1 = new ArrayList<String>(Arrays.asList("-n","1","testing_file.txt"));
        new Tail().exec(args1, null, out1);

        PipeSetter.setCmdAtPipeTail();
        ArrayList<String> args2 = new ArrayList<String>(Arrays.asList("-b","not_valid"));
        new Cut().exec(args2, null, out2);

        out1.close();
        out2.close();
    }      

}
