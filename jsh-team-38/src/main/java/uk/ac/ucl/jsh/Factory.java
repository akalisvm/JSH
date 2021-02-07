package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/* Factory: provides getApplication method for factories */
interface Factory {
    void getApplication(String appName, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException;
}
