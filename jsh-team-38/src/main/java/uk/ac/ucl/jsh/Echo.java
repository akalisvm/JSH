package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Output the arguments.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Echo implements Application {

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
        StringBuilder connectArgs = new StringBuilder();

        for(String arg: appArgs) {
            connectArgs.append(arg).append(" ");
        }

        if(connectArgs.toString().isEmpty()) {
            GlobalVariable.getInstance().addData("");
            if(GlobalVariable.getInstance().getWriteable()) {
                writer.write(" ");
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        }
        else {
            connectArgs.deleteCharAt(connectArgs.length()-1);
            GlobalVariable.getInstance().addData(connectArgs.toString());
            if(GlobalVariable.getInstance().getWriteable()) {
                writer.write(connectArgs.toString());
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        }
    }
}
