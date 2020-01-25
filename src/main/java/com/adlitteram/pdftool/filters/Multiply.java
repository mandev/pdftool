package com.adlitteram.pdftool.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Multiply extends AbstractPdfFilter {

    protected int count;

    /**
     * Multiply the document (the document is not copied)
     *
     */
    public Multiply() {
        this(1);
    }

    /**
     * Multiply by count the document (the document is not copied)
     *
     * @param count
     */
    public Multiply(int count) {
        this.count = count;
    }

    /**
     * Multiply the document (the document is not copied)
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {

        if (deleteSource) {
            inputFile.delete();
        }

        File[] outputFiles = new File[count];
        Arrays.fill(outputFiles, inputFile);
        return outputFiles;
    }

    /**
     *
     * @param srcFiles
     * @return
     */
    @Override
    public File[] filter(File[] srcFiles) {
        ArrayList<File> outputFiles = new ArrayList<>();
        for (File srcFile : srcFiles) {
            outputFiles.addAll(Arrays.asList(filter(srcFile)));
        }
        return outputFiles.toArray(new File[outputFiles.size()]);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ncount: ");
        toStringBuilder.append(count);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
