package uk.ac.ucl.jsh;

import java.io.OutputStream;

/**
 * Calls in Seq will be evaluated one by one.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Seq implements Command {

    String input;
    OutputStream output;

    Seq(String input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }
}
