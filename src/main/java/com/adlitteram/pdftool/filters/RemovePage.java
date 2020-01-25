package com.adlitteram.pdftool.filters;

import java.io.File;

public class RemovePage extends AbstractPdfFilter {

    protected String destination;
    protected String pattern;

    /**
     * Remove the selected pages from the document
     *
     * Select a range of pages. The syntax is: [!][o][odd][e][even]start-end You
     * can have multiple ranges separated by commas ','. The '!' modifier
     * removes the range from what is already selected. The range changes are
     * incremental, that is, numbers are added or deleted as the range appears.
     * The start or the end, but not both, can be omitted.
     *
     * @param selection
     */
    public RemovePage(String selection) {
        this(null, selection);
    }

    /**
     * Remove the selected pages from the document
     *
     * @param dst
     * @param pattern
     */
    public RemovePage(String dst, String pattern) {
        this.destination = dst;
        this.pattern = pattern;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        PdfFilter selectPage = new SelectPage(destination, "~" + pattern);
        return selectPage.filter(inputFile);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\npattern: ");
        toStringBuilder.append(pattern);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
