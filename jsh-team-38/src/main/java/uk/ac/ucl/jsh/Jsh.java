package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * The integrated entrance of the shell.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class Jsh {

    static void eval(String cmdline, OutputStream output) throws IOException {

        cmdline = CommandSubstitution.exec(cmdline.trim());
        CheckQuotes.exec(cmdline);

        if(GlobalVariable.getInstance().getSemiColonsIsEmpty()) {
            if(!GlobalVariable.getInstance().getVerticalBarsIsEmpty()) {
                Pipe pipe = new Pipe(cmdline, output);
                pipe.accept(new Evaluator());
            } else {
                Call call = new Call(cmdline, output);
                call.accept(new Evaluator());
            }
        } else {
            Seq seq = new Seq(cmdline, output);
            seq.accept(new Evaluator());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                eval(args[1], System.out);
            } catch (Exception e) {
                System.out.println("jsh: " + e.getMessage());
            }
        } else {
            try (Scanner input = new Scanner(System.in)) {
                while (true) {
                    String prompt = GlobalVariable.getInstance().getCurrentDirectory() + "> ";
                    System.out.print(prompt);
                    GlobalVariable.getInstance().resetGlobalVariables();
                    try {
                        String cmdline = input.nextLine();
                        if(cmdline.replace(" ", "").isEmpty()) { continue; }
                        if(!cmdline.equals("exit 0")) {
                            eval(cmdline, System.out);
                        } else { break; }
                    } catch (Exception e) {
                        System.out.println("jsh: " + e.getMessage());
                    }
                }
            }
        }
    }
}