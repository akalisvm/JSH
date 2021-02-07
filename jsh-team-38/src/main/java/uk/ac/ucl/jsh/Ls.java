package uk.ac.ucl.jsh;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Ls: output a list of directories and files in the current directory.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Ls implements Application {

    private final String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    /**
     * Excute the application.
     * 
     * @param appArgs arguments for the applications.
     * @param input The InputStream redirected to.
     * @param output The OutputStream redirected to.
     * @throws IOException catch the IO exceptions from concrete applications.
     */

    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(output);
        File currDir;
        GlobalVariable.getInstance().clearData();

        if(input != null) {
            if(appArgs.isEmpty()) {
                currDir = new File(currentDirectory);
            } else {
                currDir = new File(currentDirectory + File.separator + appArgs.get(0));
            }

            if(currDir.exists()) {
                if(currDir.isDirectory()) {
                    File[] listOfFiles = currDir.listFiles();
                    for(File file : Objects.requireNonNull(listOfFiles)) {
                        System.out.println(file.getName());
                    }
                } else {
                    System.out.println(appArgs.get(0));
                }
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if (appArgs.isEmpty()) {
                currDir = new File(currentDirectory);
            } else if (appArgs.size() == 1) {
                currDir = new File(currentDirectory + File.separator + appArgs.get(0));
            } else {
                throw new RuntimeException("ls: too many arguments");
            }


            if(currDir.exists()) {
                if(currDir.isDirectory()) {
                    File[] listOfFiles = currDir.listFiles();
                    StringBuilder sb = new StringBuilder();
                    for (File file : Objects.requireNonNull(listOfFiles)) {
                        if (!file.getName().startsWith(".")) {
                            GlobalVariable.getInstance().addData(file.getName());
                            sb.append(file.getName()).append("\t");

                        }
                    }
                    if(listOfFiles.length > 0) { sb.deleteCharAt(sb.length()-1); }
                    if(listOfFiles.length != 0 && GlobalVariable.getInstance().getWriteable()) {
                        writer.write(sb.toString());
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                } else {
                    GlobalVariable.getInstance().addData(appArgs.get(0));
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(appArgs.get(0));
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } else {
                throw new RuntimeException("ls: " + appArgs.get(0) + ": No such file or directory");
            }
        }
    }
}