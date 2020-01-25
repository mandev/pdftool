package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;

public class LineMark {

    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;
    protected float thickness;
    protected BaseColor color;

    public LineMark() {
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public LineMark(float x1, float y1, float x2, float y2) {
        this(x1, y1, x2, y2, 1f, BaseColor.BLACK);
    }

    /**
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param thickness
     * @param color
     */
    public LineMark(float x1, float y1, float x2, float y2, float thickness, BaseColor color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
        this.color = color;
    }

    /**
     * @return the x1
     */
    public float getX1() {
        return x1;
    }

    /**
     * @return the y1
     */
    public float getY1() {
        return y1;
    }

    /**
     * @return the x2
     */
    public float getX2() {
        return x2;
    }

    /**
     * @return the y2
     */
    public float getY2() {
        return y2;
    }

    /**
     * @return the width
     */
    public float getThickness() {
        return thickness;
    }

    /**
     * @return the color
     */
    public BaseColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\nx1: ");
        toStringBuilder.append(x1);
        toStringBuilder.append("\ny1: ");
        toStringBuilder.append(y1);
        toStringBuilder.append("\nx2: ");
        toStringBuilder.append(x2);
        toStringBuilder.append("\ny2: ");
        toStringBuilder.append(y2);
        toStringBuilder.append("\nthickness: ");
        toStringBuilder.append(thickness);
        toStringBuilder.append("\ncolor: ");
        toStringBuilder.append(color);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
