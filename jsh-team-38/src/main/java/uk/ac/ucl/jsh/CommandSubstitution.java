package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

/* CommandSubstitution: command substitution is performed */
public class CommandSubstitution {

    public static String exec(String cmdline) throws IOException {

        if(!GlobalVariable.getInstance().getIsSubCmd()) {
            int numOfBackQuote = 0;
            int firstBackQuote = -1;
            int lastBackQuote = -1;
            for(int i = 0; i < cmdline.length(); i++) {
                if(cmdline.charAt(i) == '`') {
                    numOfBackQuote++;
                    if(firstBackQuote == -1) {
                        firstBackQuote = i;
                    }
                    lastBackQuote = i;
                }
            }

            if(numOfBackQuote >= 2) {
                GlobalVariable.getInstance().setIsSubCmd(true);
                PipedInputStream in = new PipedInputStream();
                PipedOutputStream out = new PipedOutputStream();
                out.connect(in);
                subCmdThread(cmdline.substring(firstBackQuote+1, lastBackQuote), out);

                // parse the data transferred from outputStream
                byte[] exBytes = new byte[1024];
                int len = in.read(exBytes);
                String exMessage = new String(exBytes, 0, len);

                // if the data starts with "sub: ", throw an exception
                if(exMessage.startsWith("sub: ")) {
                    throw new RuntimeException(exMessage);
                }

                // otherwise, substitute the arguments with the new data
                else {
                    Scanner scn = new Scanner(in);
                    StringBuilder scnStr = new StringBuilder();

                    if (!exMessage.replace("\r", "").replace("\t", "").replace("\n", "").replace(" ", "").isEmpty()) {
                        int firstLineEndIndex = 0;
                        for (int i = 0; i < exMessage.length(); i++) {
                            if (exMessage.charAt(i) == '\n') {
                                firstLineEndIndex = i;
                                break;
                            }
                        }

                        String firstLine = exMessage.substring(0, firstLineEndIndex).replace("\r", "").replace("\n", "");

                        scnStr.append(firstLine).append(" ");
                        while (scn.hasNextLine()) {
                            scnStr.append(scn.nextLine()).append(" ");
                        }
                        scnStr.deleteCharAt(scnStr.length()-1);
                    }
                    scn.close();

                    String subCmdOutStr = scnStr.toString().replace("\t", " ").replace("\n", " ");
                    if(GlobalVariable.getInstance().getIsSubCmdFileOutput()) {
                        subCmdOutStr = "\'" + subCmdOutStr + "\'";
                    }
                    String argsBeforeSub = cmdline.substring(0, firstBackQuote);
                    String argsAfterSub = cmdline.substring(lastBackQuote+1);
                    cmdline = argsBeforeSub + subCmdOutStr + argsAfterSub;
                    GlobalVariable.getInstance().resetGlobalVariables();
                }
            }
        }
        return cmdline;
    }

    /* create a thread to execute the sub command */
    private static void subCmdThread(String subCmd, OutputStream output) {
        new Thread(()-> {
            try {
                Jsh.eval(subCmd, output);
                output.close();
            } catch (Exception e) {
                try {
                    //exception message in this thread starts with "sub: "
                    output.write(("sub: " + e.getMessage()).getBytes());
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
}
