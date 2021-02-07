package uk.ac.ucl.jsh;

/* CheckQuotes: check whether this character is surrounded by single quotes or double quotes */
public class CheckQuotes {

    public static void exec(String cmdline) {
        for(int i = 0; i < cmdline.length(); i++) {
            if(notSurroundedByQuotes(i, cmdline)) {
                if(cmdline.charAt(i) == ';') {
                    GlobalVariable.getInstance().addSemiColon(i);
                } else if(cmdline.charAt(i) == '|') {
                    GlobalVariable.getInstance().addVerticalBar(i);
                }
            }
        }
    }

    private static boolean notSurroundedByQuotes(int pos, String cmdline) {
        if(pos == 0) { return true; }
        else if(pos == cmdline.length()-1) { return true; }
        else if(cmdline.charAt(pos-1) == '\'' && cmdline.charAt(pos+1) == '\'') { return false; }
        else return !(cmdline.charAt(pos-1) == '\"' && cmdline.charAt(pos+1) == '\"');
    }
}
