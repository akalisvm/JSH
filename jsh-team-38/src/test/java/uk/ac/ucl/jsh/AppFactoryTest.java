package uk.ac.ucl.jsh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AppFactoryTest {
    @Before
    public void pipeResetBeforeTest(){
        JshInitializer.reset();
    }

    @After
    public void pipeResetAfterTest(){
        JshInitializer.reset();
    }

    //testing AppFactory on exceptional case - unknown app with exception (where unsafeDecorator runs successfully)
    @Test(expected = RuntimeException.class)
    public void testJsh_AppFactory_default_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("unknown_app", out);
        out.close();
    } 

    //testing unsafe on exceptional case - unknown app with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_default_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_unknown_app", out);
        assertEquals(out.toString(), "unsafe version: unknown_app: unknown application" + System.lineSeparator());
        out.close();
    } 
    
    //testing unsafe head on normal case - head with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_head_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_head", out);
        assertEquals(out.toString(), "unsafe version: head: missing arguments" + System.lineSeparator());
        out.close();
    }
    
    //testing unsafe cd on normal case - cd with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_cd_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_cd", out);
        assertEquals(out.toString(), "unsafe version: cd: missing argument" + System.lineSeparator());
        out.close();
    }

    //testing unsafe pwd on normal case
    @Test
    public void testJsh_unsafe_pwd_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_pwd extra_arg", out);
        assertEquals(out.toString(), "unsafe version: pwd: wrong number of arguments" + System.lineSeparator());
        out.close();
    }

    // //testing unsafe pwd on normal case
    // @Test
    // public void testJsh_unsafe_pwd_normal2() throws Exception {
    //     ByteArrayOutputStream out = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(out));
    //     Jsh.eval("_pwd", out);
    //     assertTrue(out.toString().endsWith("jsh-team-38"+ System.lineSeparator()));
    //     out.close();
    // }

    //testing unsafe ls on normal case - extra one arg
    @Test
    public void testJsh_unsafe_ls_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_ls arg1 extra_arg", out);
        assertEquals(out.toString(), "unsafe version: ls: too many arguments" + System.lineSeparator());
        out.close();
    }

    //testing unsafe echo on normal case - ARG
    @Test
    public void testJsh_unsafe_echo_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_echo 1", out);
        assertEquals(out.toString(), "1" + System.lineSeparator());
        out.close();
    }

    //testing unsafe tail on normal case - tail with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_tail_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_tail", out);
        assertEquals(out.toString(), "unsafe version: tail: missing arguments" + System.lineSeparator());
        out.close();
    }    

    //testing unsafe grep on normal case - grep with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_grep_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_grep", out);
        assertEquals(out.toString(), "unsafe version: grep: wrong number of arguments" + System.lineSeparator());
        out.close();
    }   
    
    //testing unsafe find on normal case - find with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_find_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_find", out);
        assertEquals(out.toString(), "unsafe version: find: wrong number of arguments" + System.lineSeparator());
        out.close();
    }    

    //testing unsafe sort on normal case - sort with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_sort_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_sort", out);
        assertEquals(out.toString(), "unsafe version: sort: wrong number of arguments" + System.lineSeparator());
        out.close();
    } 

    //testing unsafe cut on normal case - cut with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_cut_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_cut", out);
        assertEquals(out.toString(), "unsafe version: cut: wrong number of arguments" + System.lineSeparator());
        out.close();
    }     

    //testing unsafe assign on normal case - assign with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_assign_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_assign", out);
        assertEquals(out.toString(), "unsafe version: assign: missing argument" + System.lineSeparator());
        out.close();
    } 

    //testing unsafe wc on normal case - wc with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_wc_assign_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_wc", out);
        assertEquals(out.toString(), "unsafe version: wc: missing arguments" + System.lineSeparator());
        out.close();
    } 

    //testing unsafe uniq on normal case - uniq with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_uniq_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_uniq", out);
        assertEquals(out.toString(), "unsafe version: uniq: missing arguments" + System.lineSeparator());
        out.close();
    }

    //testing unsafe cat on normal case - cat with exception (where unsafeDecorator runs successfully)
    @Test
    public void testJsh_unsafe_cat_normal1() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        Jsh.eval("_cat", out);
        assertEquals(out.toString(), "unsafe version: cat: missing arguments" + System.lineSeparator());
        out.close();
    }    

}
