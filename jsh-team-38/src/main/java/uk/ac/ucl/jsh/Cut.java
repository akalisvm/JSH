package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Output all contents after cutting out the sections from each line.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Cut implements Application {

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
            if(!appArgs.get(0).equals("-b")) {
                throw new RuntimeException("cut: unknown option: '" + appArgs.get(0) + "'");
            }
            String index = checkIndex(appArgs);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                String string = cutter(index, line);
                System.out.println(string);
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) {
                printCutResult(appArgs, writer);
            } else {
                if(appArgs.size() == 2) {
                    if (!appArgs.get(0).equals("-b")) {
                        throw new RuntimeException("cut: unknown option: '" + appArgs.get(0) + "'");
                    }
                    String index = checkIndex(appArgs);
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    GlobalVariable.getInstance().clearData();
                    for(String line: storage) {
                        writer.write(cutter(index, line));
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                } else {
                    printCutResult(appArgs, writer);
                }
            }
        }
    }

    private String checkIndex(ArrayList<String> appArgs) {
        String index = appArgs.get(1);
        char[] ch = index.toCharArray();
        if (index.startsWith(",") || index.endsWith(",")) {
            throw new RuntimeException("cut: fields and positions are numbered from 1");
        }
        for (char c : ch) {
            if (c == '0') {
                throw new RuntimeException("cut: fields and positions are numbered from 1");
            }
            else if (!Character.isDigit(c) && c != '-' && c != ',') {
                throw new RuntimeException("cut: invalid byte, character or field list");
            }
        }
        return index;
    }


    private void printCutResult(ArrayList<String> appArgs, OutputStreamWriter writer) throws IOException {

        if (appArgs.size() != 3) {
            throw new RuntimeException("cut: wrong number of arguments");
        }
        if (!appArgs.get(0).equals("-b")) {
            throw new RuntimeException("cut: unknown option: '" + appArgs.get(0) + "'");
        }
        GlobalVariable.getInstance().clearData();
        String cutArg;
        cutArg = appArgs.get(2);
        File cutFile = new File(currentDirectory + File.separator + cutArg);

        if (cutFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get(currentDirectory + File.separator + cutArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                String index = checkIndex(appArgs);
                String line;
                while ((line = reader.readLine()) != null) {
                    String string = cutter(index, line);
                    GlobalVariable.getInstance().addData(string);
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(string);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new IOException("cut: cannot open '" + cutArg + "' for reading");
            }
        } else {
            throw new RuntimeException("cut: " + cutArg + ": No such file or directory");
        }
    }

    private String cutter(String index, String line) {
        Set<Integer> set = new HashSet<>();
        ArrayList<Integer> list;
        StringBuilder sb = new StringBuilder();
        String[] separates = index.split(",");
        for(String s : separates) {
            if(s.contains("-")) {
                if(s.startsWith("-") && s.endsWith("-")) {
                    if(s.length() == 1) {
                        throw new RuntimeException("cut: invalid range with no end point");
                    }
                    else {
                        throw new RuntimeException("cut: invalid byte, character or field list");
                    }
                }
                String[] parts = s.split("-");
                if(s.startsWith("-")) {
                    for(int i = 1; i <= Math.min(Integer.parseInt(parts[1]), line.length()); i++) {
                        set.add(i);
                    }
                }
                else if(s.endsWith("-") && Integer.parseInt(parts[0]) <= line.length()) {
                    for(int i = Integer.parseInt(parts[0]); i <= line.length(); i++) {
                        set.add(i);
                    }
                }
                else if(parts.length == 2) {
                    for(int i = Integer.parseInt(parts[0]);
                        i <= Math.min(Integer.parseInt(parts[1]), line.length()); i++) {
                        set.add(i);
                    }
                }
            }
            else {
                if(Integer.parseInt(s) <= line.length()) {
                    set.add(Integer.parseInt(s));
                }
            }
        }
        list = new ArrayList<>(set);
        Collections.sort(list);
        for(int i : list) {
            sb.append(line, i-1, i);
        }
        return sb.toString();
    }
}