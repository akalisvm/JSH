package uk.ac.ucl.jsh;

import java.io.IOException;

/* Command: provides accept method for commands */

interface Command {
    void accept(CommandVisitor visitor) throws IOException;
}
