package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Execute appropriate unsafe version application according to app name.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class UnsafeAppFactory implements Factory {

    /**
     * execute appropriate unsafe version application according to app name and pass in the arguments.
     * 
     * @param appName name of the application.
     * @param appArgs arguments for the applications.
     * @param input The InputStream redirected to.
     * @param output The OutputStream redirected to.
     * @throws IOException catch the IO exceptions from concrete applications.
     */


    public void getApplication(String appName, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        appName = appName.substring(1);
        switch (appName) {
            case "cd":
                AbstractDecorator unsafeCd = new UnsafeVersionDecorator(new Cd());
                unsafeCd.exec(appArgs, input, output);
                break;
            case "pwd":
                AbstractDecorator unsafePwd = new UnsafeVersionDecorator(new Pwd());
                unsafePwd.exec(appArgs, input, output);
                break;
            case "ls":
                AbstractDecorator unsafeLs = new UnsafeVersionDecorator(new Ls());
                unsafeLs.exec(appArgs, input, output);
                break;
            case "cat":
                AbstractDecorator unsafeCat = new UnsafeVersionDecorator(new Cat());
                unsafeCat.exec(appArgs, input, output);
                break;
            case "echo":
                AbstractDecorator unsafeEcho = new UnsafeVersionDecorator(new Echo());
                unsafeEcho.exec(appArgs, input, output);
                break;
            case "head":
                AbstractDecorator unsafeHead = new UnsafeVersionDecorator(new Head());
                unsafeHead.exec(appArgs, input, output);
                break;
            case "tail":
                AbstractDecorator unsafeTail = new UnsafeVersionDecorator(new Tail());
                unsafeTail.exec(appArgs, input, output);
                break;
            case "grep":
                AbstractDecorator unsafeGrep = new UnsafeVersionDecorator(new Grep());
                unsafeGrep.exec(appArgs, input, output);
                break;
            case "find":
                AbstractDecorator unsafeFind = new UnsafeVersionDecorator(new Find());
                unsafeFind.exec(appArgs, input, output);
                break;
            case "uniq":
                AbstractDecorator unsafeUniq = new UnsafeVersionDecorator(new Uniq());
                unsafeUniq.exec(appArgs, input, output);
                break;
            case "sort":
                AbstractDecorator unsafeSort = new UnsafeVersionDecorator(new Sort());
                unsafeSort.exec(appArgs, input, output);
                break;
            case "cut":
                AbstractDecorator unsafeCut = new UnsafeVersionDecorator(new Cut());
                unsafeCut.exec(appArgs, input, output);
                break;
            case "assign":
                AbstractDecorator unsafeAssign = new UnsafeVersionDecorator(new Assign());
                unsafeAssign.exec(appArgs, input, output);
                break;
            case "wc":
                AbstractDecorator unsafeWc = new UnsafeVersionDecorator(new Wc());
                unsafeWc.exec(appArgs, input, output);
                break;
            default:
                System.out.println("unsafe version: " + appName + ": unknown application");
        }
    }
}
