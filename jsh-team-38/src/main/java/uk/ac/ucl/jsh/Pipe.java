package uk.ac.ucl.jsh;

import java.io.OutputStream;

/**
 * Calls in Pipe will be evaluated concurrently.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Pipe implements Command {

    String input;
    OutputStream output;

    Pipe(String input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
