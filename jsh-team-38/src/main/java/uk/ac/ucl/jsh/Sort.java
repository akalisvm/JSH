package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Output all contents in order.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Sort implements Application {

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
                Collections.sort(storage);
                for(String s : storage) {
                    System.out.println(s);
                }
            }

            else {
                if(!appArgs.get(0).equals("-r")) {
                    throw new RuntimeException("sort: unknown option: '" + appArgs.get(0) + "'");
                }
                else {
                    ArrayList<String> storage = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        storage.add(line);
                    }
                    Collections.sort(storage);
                    reverseSort(storage);
                    for(String s : storage) {
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
                if (appArgs.size() < 1 || appArgs.size() > 2) {
                    throw new RuntimeException("sort: wrong number of arguments");
                }
                if (appArgs.size() == 2 && !appArgs.get(0).equals("-r")) {
                    throw new RuntimeException("sort: unknown option: '" + appArgs.get(0) + "'");
                }
                printSortResult(appArgs, writer);
            }

            else {
                if(appArgs.isEmpty()) {
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    Collections.sort(storage);
                    GlobalVariable.getInstance().clearData();
                    for(String line: storage) {
                        GlobalVariable.getInstance().addData(line);
                        if(GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }

                else if(appArgs.size() == 1 && appArgs.get(0).startsWith("-")) {
                    if(!appArgs.get(0).equals("-r")) {
                        throw new RuntimeException("sort: unknown option: '" + appArgs.get(0) + "'");
                    }
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    Collections.sort(storage);
                    GlobalVariable.getInstance().clearData();
                    Stack<String> stack = new Stack<>();
                    for(String line : storage) {
                        stack.push(line);
                    }
                    while(!stack.isEmpty()) {
                        String line = stack.pop();
                        GlobalVariable.getInstance().addData(line);
                        if(GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(line);
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                }

                else {
                    if (appArgs.size() > 2) {
                        throw new RuntimeException("sort: wrong number of arguments");
                    }
                    if (appArgs.size() == 2 && !appArgs.get(0).equals("-r")) {
                        throw new RuntimeException("sort: unknown option: '" + appArgs.get(0) + "'");
                    }
                    printSortResult(appArgs, writer);
                }
            }
        }
    }

    private void reverseSort(ArrayList<String> storage) {
        Stack<String> stack = new Stack<>();
        for(String tempLine : storage) { stack.push(tempLine); }
        storage.clear();
        while(!stack.isEmpty()) { storage.add(stack.pop()); }
    }

    private void printSortResult(ArrayList<String> appArgs, OutputStreamWriter writer) throws IOException {
        GlobalVariable.getInstance().clearData();
        String sortArg;
        if (appArgs.size() == 2) {
            sortArg = appArgs.get(1);
        } else {
            sortArg = appArgs.get(0);
        }
        File sortFile = new File(currentDirectory + File.separator + sortArg);
        if (sortFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get(currentDirectory + File.separator + sortArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                ArrayList<String> storage = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    storage.add(line);
                }
                if (appArgs.size() == 1) {
                    Collections.sort(storage);
                } else {
                    Collections.sort(storage);
                    reverseSort(storage);
                }
                for (String s : storage) {
                    GlobalVariable.getInstance().addData(s);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(s);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new IOException("sort: cannot open '" + sortArg + "' for reading");
            }
        } else {
            throw new RuntimeException("sort: " + sortFile + ": No such file or directory");
        }
    }
}