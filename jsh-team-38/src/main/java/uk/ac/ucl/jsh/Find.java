package uk.ac.ucl.jsh;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Output a list of files in current directory or any sub-directories that contain a specific pattern.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */ 

class Find implements Application {

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
        File currDir;
        String pattern;

        if(input != null) {
            GlobalVariable.getInstance().clearData();
            currDir = new File(checkCurrDirAndPattern(appArgs).get(0));
            pattern = checkCurrDirAndPattern(appArgs).get(1);
            if(currDir.exists()) {
                if(currDir.isDirectory()) {
                    LinkedList<File> list = new LinkedList<>(Arrays.asList(Objects.requireNonNull(currDir.listFiles())));
                    while(!list.isEmpty()) {
                        if (list.getFirst().isFile()) {
                            File file = list.getFirst();
                            String string = matcher(appArgs, pattern, file);
                            if(string != null) {
                                System.out.println(string);
                            }
                        }
                        File[] files = list.removeFirst().listFiles();
                        if(files == null) {
                            continue;
                        }
                        for(File file : Objects.requireNonNull(files)) {
                            if(file.isDirectory()) {
                                list.add(file);
                            } else {
                                String string = matcher(appArgs, pattern, file);
                                if(string != null) {
                                    System.out.println(string);
                                }
                            }
                        }
                    }
                } else {
                    String string = matcher(appArgs, pattern, currDir);
                    if(string != null) {
                        System.out.println(string);
                    }
                }
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if (appArgs.size() < 2 || appArgs.size() > 3) {
                throw new RuntimeException("find: wrong number of arguments");
            } else {
                currDir = new File(checkCurrDirAndPattern(appArgs).get(0));
                pattern = checkCurrDirAndPattern(appArgs).get(1);
            }

            GlobalVariable.getInstance().clearData();
            if(currDir.exists()) {
                if(currDir.isDirectory()) {
                    LinkedList<File> list = new LinkedList<>(Arrays.asList(Objects.requireNonNull(currDir.listFiles())));
                    while(!list.isEmpty()) {
                        if(list.getFirst().isFile()) {
                            File file = list.getFirst();
                            printFindResult(appArgs, pattern, file, writer);
                        }
                        File[] files = list.removeFirst().listFiles();
                        if(files == null) {
                            continue;
                        }
                        for(File file : Objects.requireNonNull(files)) {
                            if(file.isDirectory()) {
                                list.add(file);
                            } else {
                                printFindResult(appArgs, pattern, file, writer);
                            }
                        }
                    }
                } else {
                    printFindResult(appArgs, pattern, currDir, writer);
                }
            } else {
                throw new RuntimeException("find: " + currDir + ": No such file or directory");
            }
        }
    }

    private ArrayList<String> checkCurrDirAndPattern(ArrayList<String> appArgs) {
        ArrayList<String> list = new ArrayList<>();
        String currDir;
        String pattern;
        if (appArgs.size() == 2) {
            currDir = currentDirectory;
            if (!appArgs.get(0).equals("-name")) {
                throw new RuntimeException("find: unknown option: '" + appArgs.get(0) + "'");
            }
            pattern = appArgs.get(1);
        } else {
            currDir = currentDirectory + File.separator + appArgs.get(0);
            if (!appArgs.get(1).equals("-name")) {
                throw new RuntimeException("find: unknown option: '" + appArgs.get(1) + "'");
            }
            pattern = appArgs.get(2);
        }
        list.add(currDir);
        list.add(pattern);
        return list;
    }

    private void printFindResult(ArrayList<String> appArgs, String pattern, File file, OutputStreamWriter writer) throws IOException {
        String string = matcher(appArgs, pattern, file);
        if(string != null) {
            GlobalVariable.getInstance().addData(string);
            if (GlobalVariable.getInstance().getWriteable()) {
                writer.write(string);
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        }
    }

    private String matcher(ArrayList<String> appArgs, String pattern, File file) {
        String fileName = file.getName();
        int patternLength = pattern.length();
        int stringLength = fileName.length();
        boolean[][] dp = new boolean[patternLength+1][stringLength+1];
        dp[0][0] = true;
        for(int i = 1; i <= patternLength; i++) {
            if(pattern.charAt(i-1) != '*') {
                break;
            }
            dp[i][0] = true;
        }
        for(int i = 1; i <= patternLength; i++) {
            for(int j = 1; j <= stringLength; j++) {
                if(pattern.charAt(i-1) == fileName.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                }
                else if(pattern.charAt(i-1) == '*') {
                    dp[i][j] = dp[i-1][j] | dp[i][j-1];
                }
            }
        }
        String matchedString = null;
        if(dp[patternLength][stringLength]) {
            int numOfFileSeparator = 0;
            for(int i = 0; i < currentDirectory.length(); i++) {
                if(File.separator.equals(String.valueOf(currentDirectory.charAt(i)))) {
                    numOfFileSeparator++;
                }
            }
            char[] pathArray = file.getPath().toCharArray();
            int counter = 0;
            StringBuilder sb = new StringBuilder();
            if(appArgs.get(0).equals("-name")) { sb.append(".").append(File.separator); }
            for (int i = 0; i < pathArray.length; i++) {
                if (File.separator.equals(String.valueOf(pathArray[i]))) {
                    counter++;
                    if (counter == numOfFileSeparator+1) {
                        sb.append(file.getPath(), i+1, pathArray.length);
                    }
                }
            }
            matchedString = sb.toString();
        }
        return matchedString;
    }
}
