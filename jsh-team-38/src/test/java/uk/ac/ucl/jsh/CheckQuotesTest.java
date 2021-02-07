package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckQuotesTest {
    @Before
    public void createTemp() {
        JshInitializer.reset();
    }

    @After
    public void deleteTemp() {
        JshInitializer.reset();
    }

    //testing CheckQuotesTest on edge case
    @Test
    public void testJsh_CheckQuotes_normal1() throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        CheckQuotes.exec("\"|\"");
        assertTrue(GlobalVariable.getInstance().getVerticalBarsIsEmpty());
        out.close();
    }
}
