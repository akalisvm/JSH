package uk.ac.ucl.jsh;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Change current working directory in singleton.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Cd implements Application {

    private final String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    /**
     * Execute the application.
     * 
     * @param appArgs arguments for the applications.
     * @param input The InputStream redirected to.
     * @param output The OutputStream redirected to.
     * @throws IOException catch the IO exceptions from concrete applications.
     */
    
    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        if(GlobalVariable.getInstance().getIsNotPipe()) {
            if (appArgs.isEmpty()) {
                throw new RuntimeException("cd: missing argument");
            } else if (appArgs.size() > 1) {
                throw new RuntimeException("cd: too many arguments");
            }
            String dirArg = appArgs.get(0);
            File dir = new File(currentDirectory, dirArg);
            if (!dir.exists()) {
                throw new RuntimeException("cd: " + dirArg + " No such file or directory");
            } else if(!dir.isDirectory()) {
                throw new RuntimeException("cd: " + dirArg + " Not a directory");
            }
            GlobalVariable.getInstance().setCurrentDirectory(dir.getCanonicalPath());
        } else {
            GlobalVariable.getInstance().clearData();
        }
    }
}
