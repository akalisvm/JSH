package uk.ac.ucl.jsh;

import java.io.IOException;

/* CommandVisitor: provides visit methods for evaluator to visit Seq, Pipe and Call */

interface CommandVisitor {
    /* *
     * Each concrete element class corresponds to a visit method,
     * in which the parameter type identifies the specific element to be accessed.
     */
    void visit(Seq seq);
    void visit(Pipe pipe);
    void visit(Call call) throws IOException;
}
