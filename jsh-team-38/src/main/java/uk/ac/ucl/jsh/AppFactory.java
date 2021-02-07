package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Execute appropriate application according to app name 
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class AppFactory implements Factory {

/**
 * execute appropriate application according to app name and pass in the arguments
 * 
 * @param appName name of the application
 * @param appArgs arguments for the applications
 * @param input The inputStream redirected to
 * @param output The OutputStream redirected to
 * @throws IOException catch the IO exceptions from concrete applications
 */

    public void getApplication(String appName, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        switch (appName) {
            case "cd":
                new Cd().exec(appArgs, input, output);
                break;
            case "pwd":
                new Pwd().exec(appArgs, input, output);
                break;
            case "ls":
                new Ls().exec(appArgs, input, output);
                break;
            case "cat":
                new Cat().exec(appArgs, input, output);
                break;
            case "echo":
                new Echo().exec(appArgs, input, output);
                break;
            case "head":
                new Head().exec(appArgs, input, output);
                break;
            case "tail":
                new Tail().exec(appArgs, input, output);
                break;
            case "grep":
                new Grep().exec(appArgs, input, output);
                break;
            case "find":
                new Find().exec(appArgs, input, output);
                break;
            case "uniq":
                new Uniq().exec(appArgs, input, output);
                break;
            case "sort":
                new Sort().exec(appArgs, input, output);
                break;
            case "cut":
                new Cut().exec(appArgs, input, output);
                break;
            case "assign":
                new Assign().exec(appArgs, input, output);
                break;
            case "wc":
                new Wc().exec(appArgs, input, output);
                break;
            default:
                throw new RuntimeException(appName + ": unknown application");
        }
    }
}
