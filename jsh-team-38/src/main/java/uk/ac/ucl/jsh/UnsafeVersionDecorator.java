package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * UnsafeVersionDecorator: inherits AbstractDecorator and add functions to the applications dynamically.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class UnsafeVersionDecorator extends AbstractDecorator {

    // do the same thing as what AbstractDecorator does
    UnsafeVersionDecorator(Application decoratedApplication) {
        super(decoratedApplication);
    }

    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        try {
            decoratedApplication.exec(appArgs, input, output);
        } catch (Exception e) {
            printExceptionMessage(e, output);
        }
    }

    // extra function that output an error message instead of raising exceptions
    private void printExceptionMessage(Exception e, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        writer.write("unsafe version: " + e.getMessage());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
}

