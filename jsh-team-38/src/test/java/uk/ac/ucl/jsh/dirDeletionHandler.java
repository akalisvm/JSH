package uk.ac.ucl.jsh;

import java.io.File;

/**
 * Delete dir.
 * @author Ruochen Sun, Tianang Chen, Xiaoyan Xu.
 */

public class dirDeletionHandler {
    //recursively delete dir
    protected static boolean del_dir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = del_dir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
