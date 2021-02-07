package uk.ac.ucl.jsh;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluator: implements the visit methods declared in abstract class CommonVisitor 
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class Evaluator implements CommandVisitor {

    private String currentDirectory = GlobalVariable.getInstance().getCurrentDirectory();

    /**
     * determine the behaviours of the visitor when accessing Seq 
     * @param seq the seq passed in.
     */
    
    public void visit(Seq seq) {

        String input = seq.input;
        OutputStream output = seq.output;

        ArrayList<String> seqList = new ArrayList<>();
        ArrayList<Integer> semiColonList = GlobalVariable.getInstance().getSemiColonList();
        checkSeqExecutionSequence(input, seqList, semiColonList);
        GlobalVariable.getInstance().clearSemiColons();

        try {
            int offset = 0;
            for(String command : seqList) {
                if(command.contains("|")) {
                    // if one of command in seq includes "|" and not quoted by quotes, this command is a pipe
                    boolean pipeFlag = false;
                    for(int i = 0; i < command.length(); i++) {
                        if(command.charAt(i) == '|' && GlobalVariable.getInstance().getContainsVerticalBar(offset + i)) {
                            pipeFlag = true;
                            break;
                        }
                    }
                    if(pipeFlag) {
                        Pipe pipe = new Pipe(command, output);
                        pipe.accept(this);
                    } else {
                        Call call = new Call(command, output);
                        call.accept(this);
                    }
                    offset += command.length() + 1;
                    GlobalVariable.getInstance().updateOffset(offset);
                } else {
                    // otherwise, it's a call
                    Call call = new Call(command, output);
                    call.accept(this);
                    offset += command.length() + 1;
                    GlobalVariable.getInstance().updateOffset(offset);
                }
            }
        } catch (Exception e) {
            System.out.print("");
        }
    }

    /**
     * determine the behaviours of the visitor when accessing Pipe 
     * @param pipe the pipe passed in.
     */

    public void visit(Pipe pipe) {

        String input = pipe.input;
        OutputStream output = pipe.output;

        GlobalVariable.getInstance().setIsPipe(true);
        ArrayList<String> pipeList = new ArrayList<>();
        ArrayList<Integer> verticalBarsList = GlobalVariable.getInstance().getVerticalBarList();
        int offset = GlobalVariable.getInstance().getOffset();
        checkPipeExecutionSequence(offset, input, pipeList, verticalBarsList);
        GlobalVariable.getInstance().clearVerticalBars();

        try {
            for (int i = 0; i < pipeList.size(); i++) {
                if (i == 0) {
                    // the first call in pipe
                    GlobalVariable.getInstance().setIsPipeHead(true);
                    GlobalVariable.getInstance().setIsPipeTail(false);
                    Call call = new Call(pipeList.get(0), output);
                    call.accept(this);
                    // the rest of calls are not the first call anymore
                    GlobalVariable.getInstance().setIsPipeHead(false);
                    GlobalVariable.getInstance().setIsPipeTail(false);
                } else if (i == pipeList.size()-1) {
                    // the last call in pipe
                    GlobalVariable.getInstance().setIsPipeHead(false);
                    GlobalVariable.getInstance().setIsPipeTail(true);
                    Call call = new Call(pipeList.get(pipeList.size()-1), output);
                    call.accept(this);
                    GlobalVariable.getInstance().setIsPipeHead(false);
                    GlobalVariable.getInstance().setIsPipeTail(false);
                } else {
                    // neither the first call nor the last call in pipe
                    GlobalVariable.getInstance().setIsPipeHead(false);
                    GlobalVariable.getInstance().setIsPipeTail(false);
                    Call call = new Call(pipeList.get(i), output);
                    call.accept(this);
                    GlobalVariable.getInstance().setIsPipeHead(false);
                    GlobalVariable.getInstance().setIsPipeTail(false);
                }
            }
            // reset to false since pipe evaluating process finishes
            GlobalVariable.getInstance().setIsPipe(false);
            GlobalVariable.getInstance().setIsPipeHead(false);
            GlobalVariable.getInstance().setIsPipeTail(false);
            // clear the list to empty the outputStream of the pipe
            GlobalVariable.getInstance().clearData();
        } catch (Exception e) {
            System.out.println("");
        }
    }

    /**
     * determine the behaviours of the visitor when accessing Call 
     * @param call the call passed in.
     * @throws IOException catch the IO exceptions from concrete applications.
     */

    public void visit(Call call) throws IOException {

        String input = call.input;
        OutputStream output = call.output;

        /*
         * space regex meaning here:
         * [^\\s\"']+: one or more any character except spaces, double quotes and single quotes
         * ([^"']*?)": 0 or many characters except double quotes and single quotes, the matching strategy is Laziness
         * ([^"']*) : 0 or many characters except double quotes and single quotes, the matching strategy is Greediness
         * ([^"]*) : 0 or many characters except double quotes, the matching strategy is Greediness
         * (.*) : 0 or many any kind of characters, the matching strategy is Greediness
         * \" : one double quote character
         * ' : one single quote character
         */

        String spaceRegex = "[^\\s\"']+|([^\"']*?)\"([^\"]*)\"([^\"']*)|'(.*)'";
        ArrayList<String> tokens = new ArrayList<>();
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(input.trim());
        String nonQuote;

        // The command is split into arguments.
        while(regexMatcher.find()) {
            if (regexMatcher.group(2) != null) {
                String quoted = regexMatcher.group(0).trim();
                tokens.add(removeQuotes(quoted, '\"'));
            } else if(regexMatcher.group(4) != null) {
                String quoted = regexMatcher.group(0).trim();
                tokens.add(removeQuotes(quoted, '\''));
            } else {
                nonQuote = regexMatcher.group().trim();
                // Filenames are expanded.
                if(nonQuote.contains("*")) {
                    tokens.addAll(globbing(nonQuote));
                } else {
                    tokens.add(nonQuote);
                }
            }
        }

        // Application name is resolved.
        String appName;
        if(tokens.get(0).startsWith("<") || tokens.get(0).startsWith(">")) {
            if(tokens.get(0).equals("<") || tokens.get(0).equals(">")) {
                appName = tokens.get(2);
                tokens.remove(2);
            } else {
                appName = tokens.get(1);
                tokens.remove(1);
            }
        } else {
            appName = tokens.get(0);
            tokens.remove(0);
        }

        if(GlobalVariable.getInstance().getIsSubCmd() && GlobalVariable.getInstance().getIsNotPipe() && appName.equals("cat")) {
            GlobalVariable.getInstance().setIsSubCmdFileOutputToTrue();
        }

        // $key shall be replaced by its corresponding value.
        ArrayList<String> appArgs = assignReplacement(new ArrayList<>(tokens));

        // Specific application is executed.
        if(GlobalVariable.getInstance().getIsSubCmd()) {
            if(appName.startsWith("_")) {
                // execute unsafe version applications if name of app starts with "_"
                new UnsafeAppFactory().getApplication(appName, appArgs, null, output);
            } else {
                // execute normal version applications
                new AppFactory().getApplication(appName, appArgs, null, output);
            }
        }

        else {
            // IO-redirection is performed to redirect Input and output stream.
            StringBuilder argStr = new StringBuilder();
            for(String arg : appArgs) {
                argStr.append(arg);
            }

            PrintStream console = System.out;
            BufferedInputStream inputStream = null;
            PrintStream outputStream = console;

            if(argStr.toString().contains("<") || argStr.toString().contains(">")) {

                String inputFileName = null;
                String outputFileName = null;
                int inputSymbolPos = -1;
                int outputSymbolPos = -1;
                boolean inputSymbolConcatWithFile = false;
                boolean outputSymbolConcatWithFile = false;
                for(int i = 0; i < appArgs.size(); i++) {
                    if(appArgs.get(i).equals("<")) {
                        if(i == appArgs.size()-1) {
                            throw new RuntimeException("IO-redirection: no input file");
                        }
                        else {
                            inputSymbolPos = i;
                            inputFileName = appArgs.get(i+1);
                            if(i+2 < appArgs.size() && !appArgs.get(i+2).equals(">")) {
                                throw new RuntimeException("IO-redirection: more than one input files");
                            }
                        }
                    }
                    else if(appArgs.get(i).startsWith("<")) {
                        if(inputSymbolPos != -1) {
                            throw new RuntimeException("IO-redirection: more than one input files");
                        } else {
                            inputSymbolConcatWithFile = true;
                            inputSymbolPos = i;
                            inputFileName = appArgs.get(i).substring(1);
                        }
                    }

                    else if(appArgs.get(i).equals(">")) {
                        if(i == appArgs.size()-1) {
                            throw new RuntimeException("IO-redirection: no output file");
                        } else {
                            outputSymbolPos = i;
                            outputFileName = appArgs.get(i+1);
                            if (i+2 < appArgs.size() && !appArgs.get(i+2).equals("<")) {
                                throw new RuntimeException("IO-redirection: more than one output files");
                            }
                        }
                    } else if(appArgs.get(i).startsWith(">")) {
                        if(outputSymbolPos != -1) {
                            throw new RuntimeException("IO-redirection: more than one output files");
                        } else {
                            outputSymbolConcatWithFile = true;
                            outputSymbolPos = i;
                            outputFileName = appArgs.get(i).substring(1);
                        }
                    }
                }

                if(outputSymbolPos == -1) {
                    if(inputSymbolConcatWithFile) {
                        appArgs.remove(appArgs.get(inputSymbolPos));
                    } else {
                        appArgs.remove(appArgs.get(inputSymbolPos+1));
                        appArgs.remove(appArgs.get(inputSymbolPos));
                    }
                } else if(inputSymbolPos == -1) {
                    if(outputSymbolConcatWithFile) {
                        appArgs.remove(appArgs.get(outputSymbolPos));
                    } else {
                        appArgs.remove(appArgs.get(outputSymbolPos+1));
                        appArgs.remove(appArgs.get(outputSymbolPos));
                    }
                } else {
                    int lower;
                    int higher;
                    if(inputSymbolPos < outputSymbolPos) {
                        if(outputSymbolConcatWithFile) {
                            lower = inputSymbolPos; higher = outputSymbolPos;
                        } else {
                            lower = inputSymbolPos; higher = outputSymbolPos + 1;
                        }
                    } else {
                        if(inputSymbolConcatWithFile) {
                            lower = outputSymbolPos; higher = inputSymbolPos;
                        } else {
                            lower = outputSymbolPos; higher = inputSymbolPos + 1;
                        }
                    }
                    for(int i = higher; i >= lower; i--) {
                        appArgs.remove(appArgs.get(i));
                    }
                }

                String wrongNumExMessage = ": wrong number of arguments";
                String unavailableAppExMessage = ": unavailable for IO-redirection";
                switch (appName) {
                    case "cd":
                    case "assign":
                        throw new RuntimeException(appName + unavailableAppExMessage);
                    case "pwd":
                    case "cat":
                        if(!appArgs.isEmpty()) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "grep":
                        if(appArgs.size() != 1) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "ls":
                    case "uniq":
                    case "sort":
                    case "wc":
                        if(appArgs.size() > 1) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "head":
                    case "tail":
                        if(!appArgs.isEmpty() && appArgs.size() != 2) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "cut":
                        if(appArgs.size() != 2) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "find":
                        if(appArgs.size() != 2 && appArgs.size() != 3) {
                            throw new RuntimeException(appName + wrongNumExMessage);
                        }
                        break;
                    case "echo":
                        break;
                    default:
                        throw new RuntimeException(appName + ": unknown application");
                }

                if(inputSymbolPos != -1) {
                    String inputFilePath = currentDirectory + File.separator + inputFileName;
                    File inputFileCheck = new File(inputFilePath);
                    if(!inputFileCheck.exists()) {
                        throw new RuntimeException("IO-redirection: " + inputFileName + ": No such file or directory");
                    } else if(!inputFileCheck.isFile()) {
                        throw new RuntimeException("IO-redirection: cannot open '" + inputFileName + "' for reading");
                    }
                    inputStream = new BufferedInputStream(new FileInputStream(inputFilePath));
                    System.setIn(inputStream);
                }
                if(outputSymbolPos != -1) {
                    String outputFilePath = currentDirectory + File.separator + outputFileName;
                    File outputFileCheck = new File(outputFilePath);
                    if(!outputFileCheck.createNewFile() && !outputFileCheck.isFile()) {
                        throw new RuntimeException("IO-redirection: cannot open '" + outputFileName + "' for reading");
                    }
                    outputStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFilePath)));
                    System.setOut(outputStream);
                    System.setErr(outputStream);
                    GlobalVariable.getInstance().setIsConsole(false);
                }
            }

            // Running the application.
            if(appName.startsWith(String.valueOf('_'))) {
                // execute unsafe version applications if name of app starts with "_"
                new UnsafeAppFactory().getApplication(appName, appArgs, inputStream, outputStream);
            } else {
                // execute normal version applications
                new AppFactory().getApplication(appName, appArgs, inputStream, outputStream);
            }
            System.setOut(console);
        }
    }

    private void checkSeqExecutionSequence(String input, ArrayList<String> result, ArrayList<Integer> semiColonList) {
        if(semiColonList.size() == 1) {
            result.add(input.substring(0, semiColonList.get(0)));
            result.add(input.substring(semiColonList.get(0)+1));
        } else {
            for(int k = 0; k < semiColonList.size(); k++) {
                int i = 0, j;
                if(k == 0) {
                    j = semiColonList.get(0);
                    result.add(input.substring(i, j));
                } else if(k == semiColonList.size()-1) {
                    i = semiColonList.get(k-1);
                    j = semiColonList.get(k);
                    result.add(input.substring(i+1, j));
                    result.add(input.substring(j+1));
                } else {
                    i = semiColonList.get(k-1);
                    j = semiColonList.get(k);
                    result.add(input.substring(i+1, j));
                }
            }
        }
    }

    private void checkPipeExecutionSequence(int offset, String input, ArrayList<String> result, ArrayList<Integer> verticalBarList) {
        StringBuilder lastSubCommand = new StringBuilder();
        for(int i = 0; i < input.length(); i++) {
            if(input.charAt(i) == '|' && verticalBarList.contains(offset + i)) {
                result.add(lastSubCommand.toString());
                lastSubCommand = new StringBuilder();
            } else {
                lastSubCommand.append(input.charAt(i));
            }
        }
        result.add(lastSubCommand.toString());
    }

    private String removeQuotes(String quoted, char quoteType) {
        int firstQuote = -1;
        int lastQuote = -1;
        for(int i = 0; i < quoted.length(); i++) {
            if(quoted.charAt(i) == quoteType && firstQuote == -1) {
                firstQuote = i;
            }
            if(quoted.charAt(i) == quoteType) {
                lastQuote = i;
            }
        }
        String strBeforeQuote = quoted.substring(0, firstQuote);
        String strQuoted = quoted.substring(firstQuote+1, lastQuote);
        String strAfterQuote = quoted.substring(lastQuote+1);
        return strBeforeQuote + strQuoted + strAfterQuote;
    }

    private ArrayList<String> globbing(String nonQuote) {
        ArrayList<String> globbingResult = new ArrayList<>();
        if(nonQuote.contains(File.separator + "*")) {
            int asterisk = nonQuote.indexOf("*");
            String arg =  nonQuote.substring(0, asterisk-1);
            String pattern =  nonQuote.substring(asterisk);
            File dir = new File(currentDirectory + File.separator + arg);
            if(dir.exists() && dir.isDirectory()) {
                File[] listOfFiles = dir.listFiles();
                for(File file : Objects.requireNonNull(listOfFiles)) {
                    String globbingFile = globbingMatcher(arg, pattern, file);
                    if(globbingFile != null) {
                        globbingResult.add(globbingFile);
                    }
                }
            }
        } else {
            File dir = new File(currentDirectory);
            File[] listOfFiles = dir.listFiles();
            for(File file : Objects.requireNonNull(listOfFiles)) {
                String globbingFile = globbingMatcher(null, nonQuote, file);
                if(globbingFile != null) {
                    globbingResult.add(globbingFile);
                }
            }
        }
        if (globbingResult.isEmpty()) {
            globbingResult.add(nonQuote);
        }
        return new ArrayList<>(globbingResult);
    }

    private String globbingMatcher(String arg, String pattern, File file) {
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
            if(arg == null) {
                matchedString = fileName;
            } else {
                matchedString = arg + File.separator + fileName;
            }
        }
        return matchedString;
    }

    private boolean isLetter(char ch) { return Character.isLetter(ch); }


    private ArrayList<String> assignReplacement(ArrayList<String> appArgs) {

        ArrayList<String> assignArgs = new ArrayList<>();

        // replace $key into value
        for (String arg : appArgs) {
            if (arg.contains("$")) {
                ArrayList<String> temp = new ArrayList<>();
                StringBuilder dollarKey = new StringBuilder();
                boolean keyFlag = false;
                for (int i = 0; i < arg.length(); i++) {
                    // the combination of '$' and following letters is supposed to be a dollar key
                    if (arg.charAt(i) == '$' && i < arg.length()-1 && isLetter(arg.charAt(i+1))) {
                        keyFlag = true;
                    }
                    if (keyFlag) {
                        if(i < arg.length() - 1) {
                            dollarKey.append(arg.charAt(i));
                        } else if(arg.charAt(arg.length() - 1) != '$') {
                            dollarKey.append(arg.charAt(i));
                            temp.add(dollarKey.toString());
                            dollarKey = new StringBuilder();
                            keyFlag = false;
                        } else {
                            temp.add(dollarKey.toString());
                            temp.add("$");
                            dollarKey = new StringBuilder();
                            keyFlag = false;
                        }
                    } else {
                        temp.add(String.valueOf(arg.charAt(i)));
                    }
                }

                StringBuilder assignArg = new StringBuilder();
                for (String tempKey : temp) {
                    // look up the value corresponding to the $key and then replace $key with value
                    if (tempKey.startsWith("$") && !tempKey.endsWith("$")) {
                        String appKey = tempKey.substring(1);
                        for (String key : GlobalVariable.getInstance().getAssignKeys()) {
                            if (appKey.equals(key)) {
                                assignArg.append(GlobalVariable.getInstance().getValueByKey().get(appKey));
                            }
                        }
                    } else {
                        assignArg.append(tempKey);
                    }
                }
                assignArgs.add(assignArg.toString());
            } else {
                assignArgs.add(arg);
            }
        }
        return assignArgs;
    }
}
