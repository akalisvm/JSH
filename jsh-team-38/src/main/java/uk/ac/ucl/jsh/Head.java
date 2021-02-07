package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/** 
 * Output the first n lines contents of this file.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */ 

class Head implements Application {

    private final String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    /** 
     * excute the application.
     *
     * @param input The InputStream redirected to.
     * @param output The OutputStream redirected to.
     * @return void
     * @throws IOException from concrete applications
     */

    @Override
    public void exec(ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(output);

        if(input != null) {
            GlobalVariable.getInstance().clearData();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int headLines = checkHeadLines(appArgs);
            for(int i = 0; i < headLines; i++) {
                String line;
                if((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) {
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("head: missing arguments");
                }
                printHeadResult(appArgs, writer);
            }

            else {
                if(appArgs.isEmpty() || appArgs.size() == 2) {
                    int headLines = checkHeadLines(appArgs);
                    ArrayList<String> storage = new ArrayList<>();
                    for (int i = 0; i < headLines; i++) {
                        if(i < GlobalVariable.getInstance().getPipeBufferDataSize()) {
                            storage.add(GlobalVariable.getInstance().getData(i));
                        }
                    }
                    GlobalVariable.getInstance().clearData();
                    GlobalVariable.getInstance().addAllData(storage);
                    if(GlobalVariable.getInstance().getIsPipeTail()) {
                        for(String line: GlobalVariable.getInstance().getPipeBufferData()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                } else {
                    printHeadResult(appArgs, writer);
                }
            }
        }
    }

    private int checkHeadLines(ArrayList<String> appArgs) {
        if (appArgs.size() == 2 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: unknown option '" + appArgs.get(0) + "'");
        }
        int headLines = 10;
        if (appArgs.size() == 2) {
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: invalid number of lines: '" + appArgs.get(1) + "'");
            }
        }
        return headLines;
    }

    private void printHeadResult(ArrayList<String> appArgs, OutputStreamWriter writer) {
        if (appArgs.size() != 1 && appArgs.size() != 3) {
            throw new RuntimeException("head: wrong number of arguments");
        }
        if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: unknown option: '" + appArgs.get(0) + "'");
        }

        int headLines = 10;
        String headArg;
        if (appArgs.size() == 3) {
            try {
                headLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: invalid number of lines: '" + appArgs.get(1) + "'");
            }
            headArg = appArgs.get(2);
        } else {
            headArg = appArgs.get(0);
        }

        GlobalVariable.getInstance().clearData();
        File headFile = new File(currentDirectory + File.separator + headArg);
        if (headFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get(currentDirectory + File.separator + headArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                for (int i = 0; i < headLines; i++) {
                    String line;
                    if ((line = reader.readLine()) != null) {
                        GlobalVariable.getInstance().addData(line);
                        if(GlobalVariable.getInstance().getWriteable()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("head: cannot open '" + headArg + "' for reading");
            }
        } else {
            throw new RuntimeException("head: " + headArg + ": No such file or directory");
        }
    }
}
