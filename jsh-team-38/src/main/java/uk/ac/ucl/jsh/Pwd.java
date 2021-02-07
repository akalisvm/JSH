package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Output current working directory.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Pwd implements Application {

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
        OutputStreamWriter writer = new OutputStreamWriter(output);
        GlobalVariable.getInstance().clearData();

        if(input != null) {
            System.out.println(currentDirectory);
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if(!appArgs.isEmpty()) {
                throw new RuntimeException("pwd: wrong number of arguments");
            }

            GlobalVariable.getInstance().addData(currentDirectory);
            if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeTail()) {
                writer.write(GlobalVariable.getInstance().getCurrentDirectory());
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        }
    }
}
