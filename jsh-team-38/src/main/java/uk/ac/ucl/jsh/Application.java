package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * An interface Application used to provide a method for applications.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

interface Application {
    void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException;
}
