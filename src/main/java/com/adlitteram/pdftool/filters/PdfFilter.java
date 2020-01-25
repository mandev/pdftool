package com.adlitteram.pdftool.filters;

import java.io.File;

public interface PdfFilter {

    /**
     *
     * @param srcFile
     * @return
     */
    public File[] filter(File srcFile);

    /**
     *
     * @param srcFiles
     * @return
     */
    public File[] filter(File[] srcFiles);
}
