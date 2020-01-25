package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import java.awt.geom.AffineTransform;

public class Template implements RenderableObject {

    protected String pdfPath;
    protected int page;
    protected float x;
    protected float y;
    protected float w;
    protected float h;
    protected float borderThickness;
    protected BaseColor borderColor;
    //
    protected PdfTemplate template;

    /**
     * Draw a PDF template on the document
     *
     * @param pdfPath
     * @param page
     * @param x
     * @param y
     */
    public Template(String pdfPath, int page, float x, float y) {
        this(pdfPath, page, x, y, 0, 0, 0, BaseColor.BLACK);
    }

    /**
     * Draw a PDF template on the document
     *
     * @param pdfPath
     * @param page
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Template(String pdfPath, int page, float x, float y, float w, float h) {
        this(pdfPath, page, x, y, w, h, 0, BaseColor.BLACK);
    }

    /**
     * Draw a PDF template on the document
     *
     * @param pdfPath
     * @param page
     * @param x
     * @param y
     * @param w
     * @param h
     * @param borderThickness
     * @param borderColor
     */
    public Template(String pdfPath, int page, float x, float y, float w, float h, float borderThickness, BaseColor borderColor) {
        this.pdfPath = pdfPath;
        this.page = page;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.borderThickness = borderThickness;
        this.borderColor = borderColor;
    }

    private PdfTemplate getTemplate(PdfContentByte cb, String path, int page) {
        PdfReader reader = null;
        try {
            if (template == null) {
                reader = new PdfReader(path);
                template = cb.getPdfWriter().getImportedPage(reader, page);
            }
        }
        catch (Exception ex) {
            if (reader != null) {
                reader.close();
            }
        }
        return template;
    }

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        template = getTemplate(cb, pdfPath, page);
        if (template == null) {
            return;
        }

        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float ww = (w > 0) ? w : template.getWidth();
        float hh = (h > 0) ? h : template.getHeight();
        float xx = (x >= 0) ? x : rect.getWidth() + x - ww;
        float yy = (y >= 0) ? y : rect.getHeight() + y - hh;

        AffineTransform af = AffineTransform.getTranslateInstance(xx, yy);
        af.scale(ww / template.getWidth(), hh / template.getHeight());
        double[] mx = new double[6];
        af.getMatrix(mx);
        cb.addTemplate(template, (float) mx[0], (float) mx[1], (float) mx[2], (float) mx[3], (float) mx[4], (float) mx[5]);

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
}
