package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class IOTest{

    @BeforeClass
    public static void createTemp() throws IOException {
        
        try{
            File testing_dir = new File("tmp");
            testing_dir.mkdir();
        }catch(Exception exception){
            System.out.println("tmp has already been created");
        }

        try{
            File testing_emptyDir = new File("tmpEmpty");
            testing_emptyDir.mkdir();
        }catch(Exception exception){
            System.out.println("tmpEmpty has already been created");
        }

        try{
            File testing_oneFolder= new File("tmp_oneFolder" + File.separator + "aFolder");
            testing_oneFolder.mkdir();
        }catch(Exception exception){
            System.out.println("tmp_oneFolder has already been created");
        }

        // in tmp
        try{
            File testing_file = new File("tmp" + File.separator + "testing_file.txt");
            testing_file.createNewFile();
        }catch(Exception exception){
            System.out.println("tmp/testing_file.txt has already been created");
        }

        try{
            File testing_input1 = new File("tmp" + File.separator + "testing_input.txt");
            testing_input1.createNewFile();
            FileWriter fileWriter = new FileWriter("tmp" + File.separator + "testing_input.txt", false);
            fileWriter.write("sof" + System.lineSeparator() + "SOF" + System.lineSeparator() + "eof");
            fileWriter.flush();
            fileWriter.close();
        }catch(Exception exception){
            System.out.println("tmp/testing_input.txt has already been created");
        }

        try{
            File testing_input2 = new File("tmp" + File.separator + "testing_input_two.txt");
            testing_input2.createNewFile();
        }catch(Exception exception){
            System.out.println("tmp/testing_input_two.txt has already been created");
        }

        try{
            File testing_output1 = new File("tmp" + File.separator + "testing_output.txt");
            testing_output1.createNewFile();
        }catch(Exception exception){
            System.out.println("tmp/testing_output.txt has already been created");
        }

        try{
            File testing_output2 = new File("tmp" + File.separator + "testing_output_two.txt");
            testing_output2.createNewFile();
        }catch(Exception exception){
            System.out.println("tmp/testing_output_two.txt has already been created");
        }
    
        // in tmpEmpty
        try{
            File testing_emptyFile = new File("tmpEmpty" + File.separator + "testing_emptyFile.txt");
            testing_emptyFile.createNewFile();
        }catch(Exception exception){
            System.out.println("tmpEmpty/testing_emptyFile.txt has already been created");
        }

        try{
            File testing_output3 = new File("tmpEmpty" + File.separator + "testing_output.txt");
            testing_output3.createNewFile();
        }catch(Exception exception){
            System.out.println("tmpEmpty/testing_output.txt has already been created");
        }
    }

    @AfterClass
    public static void deleteTemp() throws IOException{
        File dir_to_del = new File("tmp");
        File dir_to_del2 = new File("tmpEmpty");
        File dir_to_del3 = new File("tmp_oneFolder");

        try{
            dirDeletionHandler.del_dir(dir_to_del);
        }catch(Exception exception){
            System.out.println("tmp cannot delete");
        }

        try{
            dirDeletionHandler.del_dir(dir_to_del2);
        }catch(Exception exception){
            System.out.println("tmpEmpty cannot delete");
        }

        try{
            dirDeletionHandler.del_dir(dir_to_del3);
        }catch(Exception exception){
            System.out.println("tmp_oneFolder cannot delete");
        }
    }

    @Before
    public void testResetBefore() throws IOException {
        JshInitializer.reset();

        FileWriter fileWriter = new FileWriter("tmp" + File.separator + "testing_output.txt", false);
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();

        FileWriter fileWriter2 = new FileWriter("tmpEmpty" + File.separator + "testing_output.txt", false);
        fileWriter2.write("");
        fileWriter2.flush();
        fileWriter2.close();

        GlobalVariable.getInstance().setCurrentDirectory("tmp");
    }

    @After
    public void testResetAfter() {
        JshInitializer.reset();
    }



    // testing edge case on normal IO
    @Test
    public void testJsh_IO_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("<testing_input.txt cat", System.out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("eof" + System.lineSeparator()));
        out.close();
    } 

    // testing edge case on normal IO
    @Test
    public void testJsh_IO_normal2() throws Exception{
        (new Call("echo 1 >testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "1");
        inputStream.close();
        reader.close();
    }     

    // testing edge case on normal IO
    @Test
    public void testJsh_IO_normal3() throws Exception{
        (new Call("echo 1 > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "1");
        inputStream.close();
        reader.close();
    }    

    // testing edge case on normal IO
    @Test
    public void testJsh_IO_normal4() throws Exception{
        (new Call("cat >testing_output.txt <testing_input.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "sof");
        inputStream.close();
        reader.close();
    }    

    // testing IO on exception - no input file
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception1() throws Exception{
        (new Call("echo <", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - no output file
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception2() throws Exception{
        (new Call("echo 1 2 < testing_input.txt > ", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - extra input file
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception3() throws Exception{
        (new Call("cat <testing_input.txt <testing_input_two.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - extra output file
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception4() throws Exception{
        (new Call("cat <testing_input.txt >testing_output.txt >testing_output_two.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - assign: unavailable for IO-redirection
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception5() throws Exception{
        (new Call("assign < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - cat with arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception6() throws Exception{
        (new Call("cat arg < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - grep without arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception7() throws Exception{
        (new Call("grep < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - wc extra arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception8() throws Exception{
        (new Call("wc -l extra_arg < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - cut without arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception9() throws Exception{
        (new Call("cut < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - find without arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception10() throws Exception{
        (new Call("find < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - tail with one arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception11() throws Exception{
        (new Call("tail -n < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - no such file
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception12() throws Exception{
        (new Call("echo 1 2 < no_such_file > testing_output.txt", System.out)).accept(new Evaluator());
    }   
    
    // testing IO on exception - unknown app
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception13() throws Exception{
        (new Call("unknown_app < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - wrong input source
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception14() throws Exception{
        (new Call("head -n 1  < src > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - wrong output source
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception15() throws Exception{
        File testing_dir = new File("tmp" + File.separator + "subfolder");
        testing_dir.mkdirs();
        (new Call("head -n 1  < testing_input.txt > subfolder", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception17() throws Exception{
        (new Call("head -n 1 < > ", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - two input files
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception18() throws Exception{
        (new Call("echo < testing_input.txt testing_input_two.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on exception - two output files
    @Test(expected = RuntimeException.class)
    public void testJsh_IO_exception19() throws Exception{
        (new Call("echo > testing_output.txt testing_output_two.txt < testing_input.txt", System.out)).accept(new Evaluator());
    }

    // testing IO on edge normal case
    @Test
    public void testJsh_IO_edge_normal1() throws Exception{
        (new Call("echo 1 2 > testing_output.txt < testing_input.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "1 2");
        inputStream.close();
        reader.close();
    }

    // testing echo on normal IO normal
    @Test
    public void testJsh_echo_IO_normal1() throws Exception{
        (new Call("echo 1 2 < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "1 2");
        inputStream.close();
        reader.close();
    }

    // testing echo on normal IO - echo
    @Test
    public void testJsh_echo_IO_normal2() throws Exception{
        (new Call("echo < testing_input.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing cat on normal IO
    @Test
    public void testJsh_cat_IO_normal1() throws Exception{
        (new Call("cat <testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "sof");
        inputStream.close();
        reader.close();
    }

    // testing cat on normal IO
    @Test
    public void testJsh_cat_IO_normal2() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("cat < testing_input.txt", out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("eof" + System.lineSeparator()));
        out.close();
    } 

    // testing find on normal IO - no such file in not_a_dir
    @Test
    public void testJsh_find_IO_normal1() throws Exception{
        (new Call("find testing_file.txt -name no_such_file < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }            

    // testing find on normal IO - two arg
    @Test
    public void testJsh_find_IO_normal2() throws Exception{
        (new Call("find -name testing_file.txt < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "." + File.separator + "testing_file.txt");
        inputStream.close();
        reader.close();
    }  

    // testing find on normal IO
    @Test
    public void testJsh_find_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("find -name testing_file.txt < testing_input.txt", out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("testing_file.txt" + System.lineSeparator()));
        out.close();
    } 
    //strange behavior
    // // testing find on normal IO
    // @Test
    // public void testJsh_find_IO_normal4() throws Exception{
    //     FileWriter fileWriter = new FileWriter("tmp" + File.separator + "testing_input.txt", false);
    //     fileWriter.write("fake input");
    //     fileWriter.flush();
    //     fileWriter.close();

    //     (new Call("find tmp -name testing_file.txt <testing_input.txt >testing_output.txt", System.out)).accept(new Evaluator());

    //     InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
    //     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    //     assertEquals(reader.readLine(), "." + File.separator + "testing_file.txt");
    //     inputStream.close();
    //     reader.close();
    // }     

    // testing find on normal IO - no such file in not_a_dir
    @Test
    public void testJsh_find_IO_normal5() throws Exception{
        (new Call("find testing_file.txt -name no_such_file < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }         

    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal1() throws Exception{
        (new Call("pwd < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(),"tmp");
        inputStream.close();
        reader.close();
    }        

    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal2() throws Exception{
        (new Call("pwd <testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertTrue(reader.readLine().endsWith("tmp"));
        inputStream.close();
        reader.close();
    } 

    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("pwd < testing_input.txt", out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("tmp" + System.lineSeparator()));
        out.close();
    } 

    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal4() throws Exception{
        (new Call("pwd <testing_input.txt >testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertTrue(reader.readLine().endsWith("tmp"));
        inputStream.close();
        reader.close();
    } 

    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal5() throws Exception{
        (new Call("< testing_input.txt pwd > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertTrue(reader.readLine().endsWith("tmp"));
        inputStream.close();
        reader.close();
    }        
    
    // testing pwd on normal IO - pwd
    @Test
    public void testJsh_pwd_IO_normal6() throws Exception{
        (new Call("> testing_output.txt pwd < testing_input.txt", System.out)).accept(new Evaluator());
        
        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "tmp");
        inputStream.close();
        reader.close();
    }     

    // testing ls on normal IO - ls [PATH]
    @Test
    public void testJsh_ls_IO_normal1() throws Exception{
        File testing_dir = new File("tmp" + File.separator + "subfolder");
        testing_dir.mkdirs();

        (new Call("ls subfolder < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
        
        dirDeletionHandler.del_dir(testing_dir);
    } 
    
    // testing ls on normal IO - ls no arg
    @Test
    public void testJsh_ls_IO_normal2() throws Exception{
        (new Call("ls < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    }  

    // testing ls on normal IO - ls not_dir
    @Test
    public void testJsh_ls_IO_normal3() throws Exception{
        (new Call("ls testing_file.txt < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "testing_file.txt");
        inputStream.close();
        reader.close();
    }    
    
    // testing ls on normal IO - no_such_dir
    @Test
    public void testJsh_ls_IO_normal4() throws Exception{
        (new Call("ls no_such_dir < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }           

    // testing ls on normal IO
    @Test
    public void testJsh_ls_IO_normal5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("ls < testing_input.txt", System.out)).accept(new Evaluator());
        assertNotNull(out.toString());
        out.close();
    } 

    // testing head on normal IO - head
    @Test
    public void testJsh_head_IO_normal1() throws Exception{
        (new Call("head -n 1 < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "sof");
        inputStream.close();
        reader.close();
    }

    // testing head on normal IO - head empty file
    @Test
    public void testJsh_head_IO_normal2() throws Exception{
        GlobalVariable.getInstance().setCurrentDirectory("tmpEmpty");

        (new Call("head -n 1 < testing_emptyFile.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }          

    // testing head on normal IO - head
    @Test
    public void testJsh_head_IO_normal3() throws Exception{
        (new Call("head < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "sof");
        inputStream.close();
        reader.close();
    }

    // testing head on normal IO
    @Test
    public void testJsh_head_IO_normal4() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("head < testing_input.txt", out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("eof" + System.lineSeparator()));
        out.close();
    } 

    // testing tail on normal IO - tail
    @Test
    public void testJsh_tail_IO_normal1() throws Exception{
        (new Call("tail -n 1 < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "eof");
        inputStream.close();
        reader.close();
    } 

    // testing tail on normal IO - tail empty file
    @Test
    public void testJsh_tail_IO_normal2() throws Exception{
        GlobalVariable.getInstance().setCurrentDirectory("tmpEmpty");

        (new Call("tail -n 1 < testing_emptyFile.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }            

    // testing tail on normal IO
    @Test
    public void testJsh_tail_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        (new Call("tail < testing_input.txt", out)).accept(new Evaluator());
        assertTrue(out.toString().endsWith("eof" + System.lineSeparator()));
        out.close();
    } 

    // testing grep on normal IO - tail pattern matched
    @Test
    public void testJsh_grep_IO_normal1() throws Exception{
        (new Call("grep sof < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertEquals(reader.readLine(), "sof");
        inputStream.close();
        reader.close();
    } 

    // testing grep on normal IO - grep pattern unmatched
    @Test
    public void testJsh_grep_IO_normal2() throws Exception{
        (new Call("grep no_such_pattern < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNull(reader.readLine());
        inputStream.close();
        reader.close();
    }  

    // testing grep on normal IO
    @Test
    public void testJsh_grep_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("grep sof < testing_input.txt", out)).accept(new Evaluator());

        assertTrue(out.toString().endsWith("sof" + System.lineSeparator()));
        out.close();
    } 

    // testing cut on normal IO - grep -b number
    @Test
    public void testJsh_cut_IO_normal1() throws Exception{
        (new Call("cut -b 1 < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    }  

    // testing cut on exception IO - grep -unknow_opt number
    @Test(expected = RuntimeException.class)
    public void testJsh_cut_IO_exception1() throws Exception{
        (new Call("cut -unknow_opt 1 < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    } 

    // testing cut on normal IO
    @Test
    public void testJsh_cut_IO_normal5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("cut -b 1 < testing_input.txt", out)).accept(new Evaluator());
        
        assertNotNull(out.toString());
        out.close();
    } 

    // testing wc on normal IO - wc no arg
    @Test
    public void testJsh_wc_IO_normal1() throws Exception{
        (new Call("wc < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing wc on normal IO - wc -l
    @Test
    public void testJsh_wc_IO_normal2() throws Exception{
        (new Call("wc -l < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing wc on normal IO - wc -w
    @Test
    public void testJsh_wc_IO_normal3() throws Exception{
        (new Call("wc -w < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing wc on normal IO - wc -c
    @Test
    public void testJsh_wc_IO_normal4() throws Exception{
        (new Call("wc -c < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    }           

    // testing wc on normal IO
    @Test
    public void testJsh_wc_IO_normal5() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("wc < testing_input.txt", System.out)).accept(new Evaluator());

        assertNotNull(out.toString());
        out.close();
    } 

    // testing sort on normal IO - sort no arg
    @Test
    public void testJsh_sort_IO_normal1() throws Exception{
        (new Call("sort < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    }    

    // testing sort on normal IO - sort -r
    @Test
    public void testJsh_sort_IO_normal2() throws Exception{
        (new Call("sort -r < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    }            

    // testing sort on normal IO
    @Test
    public void testJsh_sort_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("sort < testing_input.txt", out)).accept(new Evaluator());

        assertNotNull(out.toString());
        out.close();
    } 

    // testing sort on exception IO - sort no arg
    @Test(expected = RuntimeException.class)
    public void testJsh_sort_IO_exception1() throws Exception{
        (new Call("sort unknown_opt < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    }   

    // testing uniq on normal IO - uniq
    @Test
    public void testJsh_uniq_IO_normal1() throws Exception{
        (new Call("uniq < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing uniq on normal IO - uniq -i
    @Test
    public void testJsh_uniq_IO_normal2() throws Exception{
        (new Call("uniq -i < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());

        InputStream inputStream = new BufferedInputStream(new FileInputStream("tmp" + File.separator + "testing_output.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        assertNotNull(reader.readLine());
        inputStream.close();
        reader.close();
    } 

    // testing uniq on normal IO
    @Test
    public void testJsh_uniq_IO_normal3() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        (new Call("uniq -i < testing_input.txt", out)).accept(new Evaluator());
        
        assertNotNull(out.toString());
        out.close();
    } 

    // testing uniq on normal IO - uniq -unknown_opt
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_IO_exception1() throws Exception{
        (new Call("uniq -unknown_opt < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    } 

    // testing uniq on normal IO - uniq -unknown_opt
    @Test(expected = RuntimeException.class)
    public void testJsh_uniq_IO_exception2() throws Exception{
        (new Call("uniq -unknown_opt < testing_input.txt > testing_output.txt", System.out)).accept(new Evaluator());
    } 
}
