package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public class PageMark implements RenderableObject {

    protected float x;
    protected float y;
    protected float size;
    protected float offset;
    protected String position;
    protected float thickness;
    protected BaseColor color;

    /**
     * Draw a page mark
     *
     * @param x
     * @param y
     * @param size
     */
    public PageMark(float x, float y, float size) {
        this(x, y, size, 0, "++++");
    }

    /**
     * Draw a page mark
     *
     * @param x
     * @param y
     * @param size
     * @param offset
     * @param position + - | or space
     */
    public PageMark(float x, float y, float size, float offset, String position) {
        this(x, y, size, offset, position, 1f, BaseColor.BLACK);
    }

    /**
     * Draw a page mark
     *
     * @param x
     * @param y
     * @param size
     * @param offset
     * @param position
     * @param thickness
     * @param color
     */
    public PageMark(float x, float y, float size, float offset, String position, float thickness, BaseColor color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.offset = offset;
        this.position = position;
        this.thickness = thickness;
        this.color = color;
    }

    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return the size
     */
    public float getSize() {
        return size;
    }

    /**
     * @return the offset
     */
    public float getOffset() {
        return offset;
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
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {

        cb.setColorStroke(color);
        cb.setLineWidth(thickness);

        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float xx = (x >= 0) ? x : rect.getWidth() + x;
        float yy = (y >= 0) ? y : rect.getHeight() + y;

        char c = position.charAt(0);

        if (c == '-' || c == '+') {
            cb.moveTo(xx - offset, yy);
            cb.lineTo(xx - offset - getSize(), yy);

            cb.moveTo(xx + offset, yy);
            cb.lineTo(xx + offset + getSize(), yy);
        }

        if (c == '|' || c == '+') {
            cb.moveTo(xx, yy - offset);
            cb.lineTo(xx, yy - offset - getSize());

            cb.moveTo(xx, yy + offset);
            cb.lineTo(xx, yy + offset + getSize());
        }

        cb.stroke();
    }

    @Override
    public String toString() {
        return "PageMark{" + "x=" + x + "y=" + y + "size=" + size + "offset=" + offset + "position=" + position + "thickness=" + thickness + "color=" + color + '}';
    }
}
