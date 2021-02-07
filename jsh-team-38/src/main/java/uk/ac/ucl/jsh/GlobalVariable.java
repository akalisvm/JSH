package uk.ac.ucl.jsh;

import java.util.*;

/**
 * GlobalVariable: a general collection of all global variables may be used in this project 
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

class GlobalVariable {

    private GlobalVariable() { }

    private String currentDirectory = System.getProperty("user.dir");
    private ArrayList<String> pipeBufferData = new ArrayList<>();
    private boolean isPipe = false;
    private boolean isPipeHead = false;
    private boolean isPipeTail = false;
    private boolean isConsole = true;
    private boolean isSubCmd = false;
    private boolean isSubCmdFileOutput = false;
    private int offset = 0;
    private Map<String, String> valueByKey = new HashMap<>();
    private Set<String> assignKeys = new HashSet<>();
    private ArrayList<Integer> semiColonList = new ArrayList<>();
    private ArrayList<Integer> verticalBarList = new ArrayList<>();

    private static class InnerClassSingleton {
        private final static GlobalVariable INNER_GLOBAL_VARIABLE = new GlobalVariable();
    }

    static GlobalVariable getInstance() {
        return InnerClassSingleton.INNER_GLOBAL_VARIABLE;
    }


    public void setCurrentDirectory(String newCurrDir) {
        currentDirectory = newCurrDir;
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }


    void addData(String line) {
        pipeBufferData.add(line);
    }

    void addAllData(ArrayList<String> list) {
        pipeBufferData.addAll(list);
    }

    void clearData() {
        pipeBufferData.clear();
    }

    String getData(int index) {
        return pipeBufferData.get(index);
    }

    int getPipeBufferDataSize() {
        return pipeBufferData.size();
    }

    ArrayList<String> getPipeBufferData() {
        return pipeBufferData;
    }


    void setIsPipe(boolean bool) {
        isPipe = bool;
    }

    boolean getIsNotPipe() {
        return !isPipe;
    }


    void setIsPipeHead(boolean bool) {
        isPipeHead = bool;
    }

    boolean getIsPipeHead() {
        return isPipeHead;
    }


    void setIsPipeTail(boolean bool) {
        isPipeTail = bool;
    }

    boolean getIsPipeTail() {
        return isPipeTail;
    }


    boolean getWriteable() {
        return !isPipe || isPipeTail;
    }


    void setIsConsole(boolean bool) {
        isConsole = bool;
    }

    boolean getIsNotConsole() {
        return !isConsole;
    }


    void setIsSubCmd(boolean bool) {
        isSubCmd = bool;
    }

    boolean getIsSubCmd() {
        return isSubCmd;
    }


    void setIsSubCmdFileOutputToTrue() {
        isSubCmdFileOutput = true;
    }

    boolean getIsSubCmdFileOutput() {
        return isSubCmdFileOutput;
    }


    void updateOffset(int n) {
        offset = n;
    }

    void resetOffset() {
        offset = 0;
    }

    int getOffset() {
        return offset;
    }


    void putKeyAndVal(String key, String val) {
        valueByKey.put(key, val);
    }

    Map<String, String> getValueByKey() {
        return valueByKey;
    }


    void addKeys(String key) {
        assignKeys.add(key);
    }

    Set<String> getAssignKeys() {
        return assignKeys;
    }


    void addSemiColon(int pos) {
        semiColonList.add(pos);
    }

    void clearSemiColons() {
        semiColonList.clear();
    }

    boolean getSemiColonsIsEmpty() {
        return semiColonList.isEmpty();
    }

    ArrayList<Integer> getSemiColonList() {
        return semiColonList;
    }


    void addVerticalBar(int pos) {
        verticalBarList.add(pos);
    }

    void clearVerticalBars() { verticalBarList.clear(); }

    boolean getContainsVerticalBar(int index) {
        return verticalBarList.contains(index);
    }

    boolean getVerticalBarsIsEmpty() {
        return verticalBarList.isEmpty();
    }

    ArrayList<Integer> getVerticalBarList() {
        return verticalBarList;
    }


    void resetGlobalVariables() {
        pipeBufferData.clear();
        isPipe = false;
        isPipeHead = false;
        isPipeTail = false;
        isSubCmd = false;
        isSubCmdFileOutput = false;
        isConsole = true;
        offset = 0;
        semiColonList.clear();
        verticalBarList.clear();
    }
}
