package com.adlitteram.pdftool.filters;

import java.io.File;

public class CropMargin extends AbstractPdfFilter {
    //
    protected String destination;
    protected float left;
    protected float top;
    protected float bottom;
    protected float right;

    /**
     * Remove margin from each page of the document
     *
     * @param left
     * @param top
     * @param bottom
     * @param right
     */
    public CropMargin(float left, float top, float bottom, float right) {
        this(null, left, top, bottom, right);
    }

    /**
     * Remove margin from each page of the document
     *
     * @param dst
     * @param left
     * @param top
     * @param bottom
     * @param right
     */
    public CropMargin(String dst, float left, float top, float bottom, float right) {
        this.destination = dst;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        PdfFilter addMargin = new AddMargin(destination, -left, -top, -bottom, -right);
        return addMargin.filter(inputFile);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\nleft: ");
        toStringBuilder.append(left);
        toStringBuilder.append("\ntop: ");
        toStringBuilder.append(top);
        toStringBuilder.append("\nbottom: ");
        toStringBuilder.append(bottom);
        toStringBuilder.append("\nright: ");
        toStringBuilder.append(right);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
