package uk.ac.ucl.jsh;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Output the last n lines.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class Tail implements Application {

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
            int tailLines = checkTailLines(appArgs);
            ArrayList<String> storage = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                storage.add(line);
            }
            int index;
            if (tailLines > storage.size()) {
                index = 0;
            } else {
                index = storage.size() - tailLines;
            }
            for(int i = index; i < storage.size(); i++) {
                System.out.println(storage.get(i));
            }
            if(GlobalVariable.getInstance().getIsNotConsole()) {
                GlobalVariable.getInstance().setIsConsole(true);
                output.close();
            }
        }

        else {
            if(GlobalVariable.getInstance().getIsNotPipe() || GlobalVariable.getInstance().getIsPipeHead()) {
                if (appArgs.isEmpty()) {
                    throw new RuntimeException("tail: missing arguments");
                }
                printTailResult(appArgs, writer);
            }

            else {
                if(appArgs.isEmpty() || appArgs.size() == 2) {
                    int tailLines = checkTailLines(appArgs);
                    ArrayList<String> storage = new ArrayList<>(GlobalVariable.getInstance().getPipeBufferData());
                    GlobalVariable.getInstance().clearData();
                    int index;
                    if (tailLines > storage.size()) {
                        index = 0;
                    } else {
                        index = storage.size() - tailLines;
                    }
                    for (int i = index; i < storage.size(); i++) {
                        GlobalVariable.getInstance().addData(storage.get(i));
                        if(GlobalVariable.getInstance().getIsPipeTail()) {
                            writer.write(storage.get(i) + System.getProperty("line.separator"));
                            writer.flush();
                        }
                    }
                } else {
                    printTailResult(appArgs, writer);
                }
            }
        }
    }

    private int checkTailLines(ArrayList<String> appArgs) {
        if (appArgs.size() == 2 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("tail: unknown option: '" + appArgs.get(0) +  "'");
        }
        int tailLines = 10;
        if (appArgs.size() == 2) {
            try {
                tailLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("tail: invalid number of lines: '" + appArgs.get(1) + "'");
            }
        }
        return tailLines;
    }

    private void printTailResult(ArrayList<String> appArgs, OutputStreamWriter writer) {
        if (appArgs.size() != 1 && appArgs.size() != 3) {
            throw new RuntimeException("Tail: wrong number of arguments");
        }
        if (appArgs.size() == 3 && !appArgs.get(0).equals("-n")) {
            throw new RuntimeException("Tail: unknown option: '" + appArgs.get(0) + "'");
        }

        int tailLines = 10;
        String tailArg;
        if (appArgs.size() == 3) {
            try {
                tailLines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("Tail: invalid number of lines: '" + appArgs.get(1) + "'");
            }
            tailArg = appArgs.get(2);
        } else {
            tailArg = appArgs.get(0);
        }

        GlobalVariable.getInstance().clearData();
        File tailFile = new File(currentDirectory + File.separator + tailArg);
        if (tailFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get(currentDirectory + File.separator + tailArg);
            ArrayList<String> storage = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    storage.add(line);
                }
                int index;
                if (tailLines > storage.size()) {
                    index = 0;
                } else {
                    index = storage.size() - tailLines;
                }
                for (int i = index; i < storage.size(); i++) {
                    GlobalVariable.getInstance().addData(storage.get(i));
                    if(GlobalVariable.getInstance().getWriteable()) {
                        writer.write(storage.get(i) + System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Tail: cannot open '" + tailArg + "' for reading");
            }
        } else {
            throw new RuntimeException("Tail: " + tailArg + ": No such file or directory");
        }
    }
}
