package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Count the number of byte, words and lines of files.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Wc implements Application{

    private final String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    private int linesCountTotal = 0;
    private int wordsCountTotal = 0;
    private int bytesCountTotal = 0;

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
        int wcFlag = 0; //indicates the status of Wc application

        if(input != null) {
            GlobalVariable.getInstance().clearData();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            if(appArgs.size() == 1) {
                wcFlag = checkWcFlag(appArgs, wcFlag);
            }
            else { wcFlag = 4; }
            ArrayList<String> storage = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                storage.add(line);
            }
            int[] countArray = concreteCountMethod(storage);
            int linesCount = countArray[0];
            int wordsCount = countArray[1];
            int bytesCount = countArray[2];
            switch (wcFlag) {
                case 1:
                    System.out.println(linesCount);
                    break;
                case 2:
                    System.out.println(wordsCount);
                    break;
                case 3:
                    System.out.println(bytesCount);
                    break;
                default:
                    System.out.println(linesCount + "\t" + wordsCount + "\t" + bytesCount);
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) {
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("wc: missing arguments");
                }
                else {
                    if(appArgs.size() == 1 && (appArgs.get(0).equals("-l") || appArgs.get(0).equals("-w") || appArgs.get(0).equals("-c"))) {
                        throw new RuntimeException("wc: wrong number of arguments");
                    }
                    printWcResult(checkWcFlag(appArgs, wcFlag), appArgs, writer);
                }
            }

            else {
                if(appArgs.size() == 1) {
                    wcFlag = checkWcFlag(appArgs, wcFlag);
                }
                else if(appArgs.isEmpty()) { wcFlag = 4; }
                int[] countArray = concreteCountMethod(GlobalVariable.getInstance().getPipeBufferData());
                int linesCount = countArray[0];
                int wordsCount = countArray[1];
                int bytesCount = countArray[2];
                String wcOutput;
                GlobalVariable.getInstance().clearData();
                switch (wcFlag) {
                    case 1:
                        wcOutput = String.valueOf(linesCount);
                        GlobalVariable.getInstance().addData(wcOutput);
                        if (GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(String.valueOf(linesCount));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                        break;
                    case 2:
                        wcOutput = String.valueOf(wordsCount);
                        GlobalVariable.getInstance().addData(wcOutput);
                        if (GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(String.valueOf(wordsCount));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                        break;
                    case 3:
                        wcOutput = String.valueOf(bytesCount);
                        GlobalVariable.getInstance().addData(wcOutput);
                        if (GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(String.valueOf(bytesCount));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                        break;
                    case 4:
                        wcOutput = linesCount + "\t" + wordsCount + "\t" + bytesCount;
                        GlobalVariable.getInstance().addData(wcOutput);
                        if (GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(String.valueOf(linesCount));
                            writer.write("\t");
                            writer.write(String.valueOf(wordsCount));
                            writer.write("\t");
                            writer.write(String.valueOf(bytesCount));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                        break;
                    default:
                        printWcResult(checkWcFlag(appArgs, wcFlag), appArgs, writer);
                }
            }
        }
    }

    private int checkWcFlag(ArrayList<String> appArgs, int wcFlag) {
        if(appArgs.get(0).equals("-l")) {
            wcFlag = 1; //only count the lines of files
        }
        else if(appArgs.get(0).equals("-w")) {
            wcFlag = 2; //only count the words of files
        }
        else if(appArgs.get(0).equals("-c")) {
            wcFlag = 3; //only count the bytes of files
        }
        else if(appArgs.get(0).startsWith("-")) {
            throw new RuntimeException("wc: unknown option: '" + appArgs.get(0) + "'");
        }
        return wcFlag;
    }

    private void printWcResult(int wcFlag, ArrayList<String> appArgs, OutputStreamWriter writer) throws IOException {

        GlobalVariable.getInstance().clearData();
        if(wcFlag == 1 || wcFlag == 2|| wcFlag == 3) {
            appArgs.remove(appArgs.get(0));
        }
        int numOfFiles = 0;
        for(String arg : appArgs) {
            Charset encoding = StandardCharsets.UTF_8;
            File currFile = new File(currentDirectory + File.separator + arg);
            if (currFile.exists()) {
                Path filePath = Paths.get(currentDirectory + File.separator + arg);
                counter(wcFlag, arg, encoding, filePath, writer);
                numOfFiles++;
            } else {
                throw new RuntimeException("wc: " + arg + ": No such file or directory");
            }
        }

        String wcOutput;
        if(numOfFiles > 1) {
            switch (wcFlag) {
                case 1:
                    wcOutput = linesCountTotal + "\t" + "total";
                    GlobalVariable.getInstance().addData(wcOutput);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(linesCountTotal));
                        writer.write("\t");
                        writer.write("total");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                    break;
                case 2:
                    wcOutput = wordsCountTotal + "\t" + "total";
                    GlobalVariable.getInstance().addData(wcOutput);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(wordsCountTotal));
                        writer.write("\t");
                        writer.write("total");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                    break;
                case 3:
                    wcOutput = bytesCountTotal + "\t" + "total";
                    GlobalVariable.getInstance().addData(wcOutput);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(bytesCountTotal));
                        writer.write("\t");
                        writer.write("total");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                    break;
                default:
                    wcOutput = linesCountTotal + "\t" + wordsCountTotal + "\t" + bytesCountTotal + "\t" + "total";
                    GlobalVariable.getInstance().addData(wcOutput);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(linesCountTotal));
                        writer.write("\t");
                        writer.write(String.valueOf(wordsCountTotal));
                        writer.write("\t");
                        writer.write(String.valueOf(bytesCountTotal));
                        writer.write("\t");
                        writer.write("total");
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
            }
        }
    }

    private void counter(int wcFlag, String arg, Charset encoding, Path filePath, OutputStreamWriter writer) {

        try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
            String line;
            ArrayList<String> storage = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                storage.add(line);
            }
            int[] countArray = concreteCountMethod(storage);
            int linesCount = countArray[0];
            int wordsCount = countArray[1];
            int bytesCount = countArray[2];
            String wcOutput;
            switch (wcFlag) {
                case 1:
                    wcOutput = linesCount + "\t" + arg;
                    GlobalVariable.getInstance().addData(wcOutput);
                    if (GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(linesCount));
                        writer.write("\t");
                        writer.write(arg);
                        writer.flush();
                    }
                    break;
                case 2:
                    wcOutput = wordsCount + "\t" + arg;
                    GlobalVariable.getInstance().addData(wcOutput);
                    if (GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(wordsCount));
                        writer.write("\t");
                        writer.write(arg);
                        writer.flush();
                    }
                    break;
                case 3:
                    wcOutput = bytesCount + "\t" + arg;
                    GlobalVariable.getInstance().addData(wcOutput);
                    if (GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(bytesCount));
                        writer.write("\t");
                        writer.write(arg);
                        writer.flush();
                    }
                    break;
                default:
                    wcOutput = linesCount + "\t" + wordsCount + "\t" + bytesCount + "\t" + arg;
                    GlobalVariable.getInstance().addData(wcOutput);
                    if (GlobalVariable.getInstance().getWriteable()) {
                        writer.write(String.valueOf(linesCount));
                        writer.write("\t");
                        writer.write(String.valueOf(wordsCount));
                        writer.write("\t");
                        writer.write(String.valueOf(bytesCount));
                        writer.write("\t");
                        writer.write(arg);
                        writer.flush();
                    }
            }
            linesCountTotal += linesCount;
            wordsCountTotal += wordsCount;
            bytesCountTotal += bytesCount;
            if(GlobalVariable.getInstance().getWriteable()) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("wc: cannot open '" + arg + "' for reading");
        }
    }

    private int[] concreteCountMethod(ArrayList<String> list) {

        /*
         * count of lines ==> countArray[0]
         * count of words ==> countArray[1]
         * count of bytes ==> countArray[2]
         */

        int[] countArray = new int[3];
        for(String line : list) {
            countArray[0]++;
            StringBuilder words = new StringBuilder();
            for(int i = 0; i < line.length(); i++) {
                if(line.charAt(i) != ' ') {
                    words.append(line.charAt(i));
                } else {
                    countArray[1]++;
                    countArray[2] += words.toString().getBytes().length+1;
                    words = new StringBuilder();
                }
            }
            countArray[1]++;
            countArray[2] += words.toString().getBytes().length+1;
        }
        return countArray;
    }
}
