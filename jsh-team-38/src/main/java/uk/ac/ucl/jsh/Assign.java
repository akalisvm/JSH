package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Assign a string to a variable.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */ 

public class Assign implements Application{
    
    /**
     * Execute the application.
     * 
     * @param appArgs arguments for the applications.
     * @param input The InputStream redirected to.
     * @param output The OutputStream redirected to.
     */

    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) {

        if(GlobalVariable.getInstance().getIsNotPipe()) {
            if (appArgs.isEmpty()) {
                throw new RuntimeException("assign: missing argument");
            }
            for(String arg : appArgs) {
                StringBuilder key = new StringBuilder();
                StringBuilder val = new StringBuilder();
                boolean assignFlag = false;
                for(int i = 0; i < arg.length(); i++) {
                    if(arg.charAt(i) == '=') {
                        assignFlag = true;
                        if(i == 0) {
                            throw new RuntimeException("assign: no key");
                        }
                        else if(i == arg.length()-1) {
                            throw new RuntimeException("assign: no value");
                        }
                    }
                    else if(!assignFlag) {
                        if(Character.isLetter(arg.charAt(i))) {
                            key.append(arg.charAt(i));
                        } else {
                            throw new RuntimeException("assign: key only contains letter");
                        }
                    }
                    else {
                        val.append(arg.charAt(i));
                    }
                }
                if(!assignFlag) {
                    throw new RuntimeException("assign: no equal operator");
                }
                GlobalVariable.getInstance().putKeyAndVal(key.toString(), val.toString());
                GlobalVariable.getInstance().addKeys(key.toString());
            }
        } else {
            GlobalVariable.getInstance().clearData();
        }
    }
}
