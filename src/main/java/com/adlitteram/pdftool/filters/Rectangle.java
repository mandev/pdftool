package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public class Rectangle implements RenderableObject {

    protected float x1;
    protected float y1;
    protected float x2;
    protected float y2;
    protected float thickness;
    protected BaseColor color;
    protected boolean fill;
    protected BaseColor colorFill;

    /**
     * Draw a rectangle
     *
     */
    public Rectangle() {
    }

    /**
     * Draw a rectangle
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public Rectangle(float x1, float y1, float x2, float y2) {
        this(x1, y1, x2, y2, 1f, BaseColor.BLACK, false, BaseColor.BLACK);
    }

    /**
     * Draw a rectangle
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param thickness
     * @param color
     * @param fill
     * @param colorFill
     */
    public Rectangle(float x1, float y1, float x2, float y2, float thickness, BaseColor color, boolean fill, BaseColor colorFill) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
        this.color = color;
        this.fill = fill;
        this.colorFill = colorFill;
    }

    /**
     *
     * @return
     */
    public float getWidth() {
        return Math.abs(x2 - x1);
    }

    /**
     *
     * @return
     */
    public float getHeight() {
        return Math.abs(y2 - y1);
    }

    /**
     *
     * @return
     */
    public float getThickness() {
        return thickness;
    }

    /**
     *
     * @return
     */
    public BaseColor getColor() {
        return color;
    }

    /**
     *
     * @return
     */
    public boolean isFill() {
        return fill;
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

        cb.setColorFill(colorFill);
        cb.setColorStroke(color);
        cb.setLineWidth(thickness);
        cb.moveTo(xx1, yy1);
        cb.lineTo(xx2, yy1);
        cb.lineTo(xx2, yy2);
        cb.lineTo(xx1, yy2);
        if (fill) {
            cb.closePathFillStroke();
        }
        else {
            cb.closePath();
        }
        cb.stroke();
    }
}
