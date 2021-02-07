package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Output all contents in the file.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Cat implements Application {

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

        if(input != null) {
            GlobalVariable.getInstance().clearData();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if ((GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) && appArgs.isEmpty()) {
                throw new RuntimeException("cat: missing arguments");
            }

            else {
                if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) {
                    printCatResult(appArgs, writer);
                } else {
                    if(appArgs.isEmpty() && GlobalVariable.getInstance().getIsPipeTail()) {
                        for(String line: GlobalVariable.getInstance().getPipeBufferData()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } else if(!appArgs.isEmpty()) {
                        printCatResult(appArgs, writer);
                    }
                }
            }
        }
    }

    private void printCatResult(ArrayList<String> appArgs, OutputStreamWriter writer) {

        GlobalVariable.getInstance().clearData();
        for(String arg : appArgs) {
            Charset encoding = StandardCharsets.UTF_8;
            File currFile = new File(currentDirectory + File.separator + arg);
            if (currFile.exists()) {
                Path filePath = Paths.get(currentDirectory + File.separator + arg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        GlobalVariable.getInstance().addData(line);
                        if (GlobalVariable.getInstance().getWriteable()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("cat: cannot open '" + arg + "' for reading");
                }
            } else {
                throw new RuntimeException("cat: " + arg + ": No such file or directory");
            }
        }
    }
}
