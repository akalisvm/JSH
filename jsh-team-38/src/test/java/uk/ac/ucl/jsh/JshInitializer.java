package uk.ac.ucl.jsh;

/**
 * Initialize the jsh.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class JshInitializer {

    public static void reset(){
        PipeSetter.reset();

        GlobalVariable.getInstance().resetOffset();
        GlobalVariable.getInstance().clearData();
        GlobalVariable.getInstance().clearSemiColons();
        GlobalVariable.getInstance().clearVerticalBars();
        
        GlobalVariable.getInstance().setCurrentDirectory(System.getProperty("user.dir"));
        GlobalVariable.getInstance().setIsSubCmd(false);
        GlobalVariable.getInstance().setIsConsole(true);
    }
}
