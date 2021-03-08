package org.tomw.fileutils;

import java.io.File;

public interface FileSelector {
    /**
     * decide if file is selected or not
     *
     * @param f file
     * @return true if file is selected
     */
    boolean select(File f);
}
