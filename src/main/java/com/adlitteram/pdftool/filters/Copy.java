package com.adlitteram.pdftool.filters;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;

public class Copy extends AbstractPdfFilter {
    //

    protected String destination;
    protected int count;

    /**
     * Copy the document
     */
    public Copy() {
        this(null, 1);
    }

    /**
     * Copy the document
     *
     * @param number
     */
    public Copy(int number) {
        this(null, number);
    }

    /**
     * Copy the document
     *
     * @param dst
     */
    public Copy(String dst) {
        this(dst, 1);
    }

    /**
     * Copy the document
     *
     * @param dst
     * @param number
     */
    public Copy(String dst, int number) {
        this.destination = dst;
        this.count = number;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        Set outputFiles = new LinkedHashSet();

        try {
            for (int i = 1; i <= count; i++) {
                File outputFile = getOutputFile(destination, inputFile, String.valueOf(i));
                File tmpFile = getTmpFile(inputFile, outputFile);
                FileUtils.copyFile(inputFile, tmpFile);
                moveFile(tmpFile, outputFile);
                outputFiles.add(outputFile);
            }

            if (deleteSource) {
                inputFile.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\n");
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\ncount: ");
        toStringBuilder.append(count);
        return toStringBuilder.toString();
    }
}
