package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Output all contents after removing the adjacent duplicate lines.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Uniq implements Application {

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
            if(appArgs.isEmpty()) {
                ArrayList<String> storage = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    storage.add(line);
                }
                ArrayList<String> result = new ArrayList<>();
                uniqCaseSensitive(storage, result);
                for(String s : result) {
                    System.out.println(s);
                }
            }

            else {
                if(!appArgs.get(0).equals("-i")) {
                    throw new RuntimeException("uniq: unknown option: '" + appArgs.get(0) + "'");
                } else {
                    ArrayList<String> storage = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        storage.add(line);
                    }
                    ArrayList<String> result = new ArrayList<>();
                    uniqCaseInsensitive(storage, result);
                    for(String s : result) {
                        System.out.println(s);
                    }
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
                    throw new RuntimeException("uniq: missing arguments");
                } else if(appArgs.size() > 2) {
                    throw new RuntimeException("uniq: wrong number of arguments");
                } else if (appArgs.size() == 2 && !appArgs.get(0).equals("-i")) {
                    throw new RuntimeException("uniq: unknown option: '" + appArgs.get(0) + "'");
                }
                printUniqResult(appArgs, writer);
            }

            else {
                if(appArgs.isEmpty()) {
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    ArrayList<String> result = new ArrayList<>();
                    uniqCaseSensitive(storage, result);
                    GlobalVariable.getInstance().clearData();
                    for (String s : result) {
                        GlobalVariable.getInstance().addData(s);
                        if(GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(s);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }

                else if(appArgs.size() == 1 && appArgs.get(0).startsWith("-")) {
                    if(appArgs.get(0).startsWith("-") && !appArgs.get(0).equals("-i")) {
                        throw new RuntimeException("uniq: unknown option: " + appArgs.get(0) + "'");
                    }
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    ArrayList<String> result = new ArrayList<>();
                    uniqCaseInsensitive(storage, result);
                    GlobalVariable.getInstance().clearData();
                    for (String s : result) {
                        GlobalVariable.getInstance().addData(s);
                        if(GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(s);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }

                else {
                    if (appArgs.size() > 2) {
                        throw new RuntimeException("uniq: wrong number of arguments");
                    }
                    if (appArgs.size() == 2 && !appArgs.get(0).equals("-i")) {
                        throw new RuntimeException("uniq: unknown option: '" + appArgs.get(0) + "'");
                    }
                    printUniqResult(appArgs, writer);
                }
            }
        }
    }

    private void printUniqResult(ArrayList<String> appArgs, OutputStreamWriter writer) {

        GlobalVariable.getInstance().clearData();
        String uniqArg;
        if (appArgs.size() == 2) {
            uniqArg = appArgs.get(1);
        } else {
            uniqArg = appArgs.get(0);
        }

        File uniqFile = new File(currentDirectory + File.separator + uniqArg);
        if (uniqFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get(currentDirectory + File.separator + uniqArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                ArrayList<String> storage = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    storage.add(line);
                }
                ArrayList<String> result = new ArrayList<>();

                if (appArgs.size() == 1) { uniqCaseSensitive(storage, result); }
                else { uniqCaseInsensitive(storage, result); }

                for (String s : result) {
                    GlobalVariable.getInstance().addData(s);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(s);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("uniq: cannot open '" + uniqArg + "' for reading");
            }
        } else {
            throw new RuntimeException("uniq: " + uniqFile + ": No such file or directory");
        }
    }

    private static void uniqCaseInsensitive(ArrayList<String> storage, ArrayList<String> result) {
        String previous = storage.get(0);
        result.add(storage.get(0));
        for (int i = 1; i < storage.size(); i++) {
            if (!storage.get(i).toLowerCase().equals(previous.toLowerCase())) {
                result.add(storage.get(i));
                previous = storage.get(i);
            }
        }
    }

    private static void uniqCaseSensitive(ArrayList<String> storage, ArrayList<String> result) {
        String previous = storage.get(0);
        result.add(storage.get(0));
        for (int i = 1; i < storage.size(); i++) {
            if (!storage.get(i).equals(previous)) {
                result.add(storage.get(i));
                previous = storage.get(i);
            }
        }
    }
}
