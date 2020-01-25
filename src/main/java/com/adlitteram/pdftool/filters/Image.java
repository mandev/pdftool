package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public class Image implements RenderableObject {

    protected String imagePath;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected float borderThickness;
    protected BaseColor borderColor;
    //
    protected com.itextpdf.text.Image image;

    /**
     * Draw an image to the document
     *
     * @param imagePath
     * @param x
     * @param y
     */
    public Image(String imagePath, float x, float y) {
        this(imagePath, x, y, 0, 0, 0, BaseColor.BLACK);
    }

    /**
     * Draw an image to the document
     *
     * @param imagePath
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Image(String imagePath, float x, float y, float w, float h) {
        this(imagePath, x, y, w, h, 0, BaseColor.BLACK);
    }

    /**
     * Draw an image to the document
     *
     * @param imagePath
     * @param x
     * @param y
     * @param w
     * @param h
     * @param borderThickness
     * @param borderColor
     */
    public Image(String imagePath, float x, float y, float w, float h, float borderThickness, BaseColor borderColor) {
        this.imagePath = imagePath;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
    }

    private com.itextpdf.text.Image getImage(String path) {
        try {
            if (image == null) {
                return com.itextpdf.text.Image.getInstance(path);
            }
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
        return image;
    }

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        image = getImage(imagePath);
        if (image == null) {
            return;
        }

        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float ww = (w > 0) ? w : image.getWidth();
        float hh = (h > 0) ? h : image.getHeight();
        float xx = (x >= 0) ? x : rect.getWidth() + x - ww;
        float yy = (y >= 0) ? y : rect.getHeight() + y - hh;

        try {
            cb.addImage(image, ww, 0, 0, hh, xx, yy);
            if (borderThickness > 0) {
                cb.setColorFill(borderColor);
                cb.setColorStroke(borderColor);
                cb.setLineWidth(borderThickness);
                cb.moveTo(xx, yy);
                cb.lineTo(xx + ww, yy);
                cb.lineTo(xx + ww, yy + hh);
                cb.lineTo(xx, yy + hh);
                cb.closePath();
                cb.stroke();
            }
        }
        catch (DocumentException ex) {
            System.err.println(ex);
        }
    }
}
