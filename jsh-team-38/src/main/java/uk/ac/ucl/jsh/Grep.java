package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cut out the sections from each line of files and writing the result to standard output.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Grep implements Application {

    private final String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    /** 
     * Execute the application.
     *
     * @param appArgs Arguments of the applications.
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
            Pattern grepPattern = Pattern.compile(appArgs.get(0));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = grepPattern.matcher(line);
                if(matcher.find()) {
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
                if (appArgs.size() < 2) {
                    throw new RuntimeException("grep: wrong number of arguments");
                }
                printGrepResult(appArgs, writer);
            }

            else {
                if (appArgs.size() == 1) {
                    Pattern grepPattern = Pattern.compile(appArgs.get(0));
                    ArrayList<String> storage = new ArrayList<>();
                    for(String line: GlobalVariable.getInstance().getPipeBufferData()) {
                        Matcher matcher = grepPattern.matcher(line);
                        if(matcher.find()) {
                            storage.add(line);
                            if(GlobalVariable.getInstance().getIsPipeTail()) {
                                writer.write(line);
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                            }
                        }
                    }
                    GlobalVariable.getInstance().clearData();
                    GlobalVariable.getInstance().addAllData(storage);
                }

                else {
                    if (appArgs.size() < 2) {
                        throw new RuntimeException("grep: wrong number of arguments");
                    }
                    printGrepResult(appArgs, writer);
                }
            }
        }
    }

    private void printGrepResult(ArrayList<String> appArgs, OutputStreamWriter writer) {

        GlobalVariable.getInstance().clearData();
        Pattern grepPattern = Pattern.compile(appArgs.get(0));
        appArgs.remove(appArgs.get(0));

        for(String arg : appArgs) {
            Charset encoding = StandardCharsets.UTF_8;
            File currFile = new File(currentDirectory + File.separator + arg);
            if(currFile.exists()) {
                Path filePath = Paths.get(currentDirectory + File.separator + arg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Matcher matcher = grepPattern.matcher(line);
                        if (matcher.find()) {
                            StringBuilder sb = new StringBuilder();
                            if (appArgs.size() > 1) {
                                sb.append(arg);
                                sb.append(":");
                                if(GlobalVariable.getInstance().getWriteable()) {
                                    writer.write(arg);
                                    writer.write(":");
                                }
                            }
                            sb.append(line);
                            if(GlobalVariable.getInstance().getWriteable()) {
                                writer.write(line);
                                writer.write(System.getProperty("line.separator"));
                                writer.flush();
                            }
                            GlobalVariable.getInstance().addData(sb.toString());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("grep: cannot open '" + arg + "' for reading");
                }
            } else {
                throw new RuntimeException("grep: " + arg + ": No such file or directory");
            }
        }
    }
}
