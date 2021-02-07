package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The base unit of execution, each call contains one application to execute.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Call implements Command {

    String input;
    OutputStream output;

    /** 
     * Splits the input into application name and arguments.
     *
     * @param input String waiting for execution from command line or split from Seq or Pipe.
     * @param output The OutputStream redirected to.
     * @return void
     */

    Call(String input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void accept(CommandVisitor visitor) throws IOException {
        visitor.visit(this);
    }
}
