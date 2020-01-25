package com.adlitteram.pdftool.filters;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;

public class WaterMark implements RenderableObject {

    protected String text;
    protected int fontSize;
    protected double opacity;
    protected BaseFont bf;

    /**
     * Draw a wartermark
     *
     * @param text
     * @param fontSize
     * @param opacity
     */
    public WaterMark(String text, int fontSize, double opacity) {
        this.text = text;
        this.fontSize = fontSize;
        this.opacity = opacity;
    }

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        if (bf == null) {
            bf = getDefaultFont();
        }

        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity((float) opacity);

        float txtwidth = bf.getWidthPoint(text, fontSize);
        Rectangle rect = reader.getCropBox(page);
        float winkel = (float) Math.atan(rect.getHeight() / rect.getWidth());
        float m1 = (float) Math.cos(winkel);
        float m2 = (float) -Math.sin(winkel);
        float m3 = (float) Math.sin(winkel);
        float m4 = (float) Math.cos(winkel);
        float xoff = (float) (-Math.cos(winkel) * txtwidth / 2 - Math.sin(winkel) * fontSize / 2);
        float yoff = (float) (Math.sin(winkel) * txtwidth / 2 - Math.cos(winkel) * fontSize / 2);

        cb.setGState(gs1);
        cb.beginText();
        if (bf != null) {
            cb.setFontAndSize(bf, fontSize);
        }
        cb.setTextMatrix(m1, m2, m3, m4, xoff + rect.getWidth() / 2, yoff + rect.getHeight() / 2);
        cb.showText(text);
        cb.endText();
    }

    private BaseFont getDefaultFont() {
        try {
            return BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "WaterMark{" + "text=" + text + "fontSize=" + fontSize + "opacity=" + opacity + "bf=" + bf + '}';
    }
}
