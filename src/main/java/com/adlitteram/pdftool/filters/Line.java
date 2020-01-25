package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public class Line implements RenderableObject {

    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;
    protected float thickness;
    protected BaseColor color;

    /**
     * Draw a line
     *
     */
    public Line() {
    }

    /**
     * Draw a line
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public Line(float x1, float y1, float x2, float y2) {
        this(x1, y1, x2, y2, 1f, BaseColor.BLACK);
    }

    /**
     * Draw a line
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param thickness
     * @param color
     */
    public Line(float x1, float y1, float x2, float y2, float thickness, BaseColor color) {
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

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float xx1 = (x1 >= 0) ? x1 : rect.getWidth() + x1;
        float yy1 = (y1 >= 0) ? y1 : rect.getHeight() + y1;
        float xx2 = (x2 >= 0) ? x2 : rect.getWidth() + x2;
        float yy2 = (y2 >= 0) ? y2 : rect.getHeight() + y2;

        cb.setColorStroke(getColor());
        cb.setLineWidth(getThickness());
        cb.moveTo(xx1, yy1);
        cb.lineTo(xx2, yy2);
        cb.stroke();
    }
}
