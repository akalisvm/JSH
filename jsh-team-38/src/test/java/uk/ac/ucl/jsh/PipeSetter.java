package uk.ac.ucl.jsh;

/**
 * Change the state of the pipe.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class PipeSetter {

    // setting flags to state: head of pipe
    protected static void setCmdAtPipeHead() {
        GlobalVariable.getInstance().setIsPipeHead(true);
        GlobalVariable.getInstance().setIsPipe(true);
        GlobalVariable.getInstance().setIsPipeTail(false);
    }

    // setting flags to state: middle of pipe
    protected static void setCmdAtPipeMid() {
        GlobalVariable.getInstance().setIsPipeHead(false);
        GlobalVariable.getInstance().setIsPipe(true);
        GlobalVariable.getInstance().setIsPipeTail(false);
    }

    // setting flags to state: tail of pipe
    protected static void setCmdAtPipeTail() {
        GlobalVariable.getInstance().setIsPipeHead(false);
        GlobalVariable.getInstance().setIsPipe(true);
        GlobalVariable.getInstance().setIsPipeTail(true);
    }

    protected static void reset() {
        GlobalVariable.getInstance().setIsPipeHead(false);
        GlobalVariable.getInstance().setIsPipe(false);
        GlobalVariable.getInstance().setIsPipeTail(false);
    }

}
