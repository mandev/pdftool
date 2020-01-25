package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public class Text implements RenderableObject {

    public static int ALIGN_LEFT = 0;
    public static int ALIGN_RIGHT = 1;
    public static int ALIGN_CENTER = 2;
    //
    protected String text;
    protected float x;
    protected float y;
    protected double rotate;
    protected int align;
    protected String fontName;
    protected int fontSize;
    protected BaseColor color;
    protected BaseFont bf;

    /**
     * Draw some text on the document
     *
     * @param text
     * @param x
     * @param y
     */
    public Text(String text, float x, float y) {
        this(text, x, y, 0, ALIGN_LEFT, null, 10, BaseColor.BLACK);
    }

    /**
     * Draw some text on the document
     *
     * @param text
     * @param x
     * @param y
     * @param rotate
     * @param align
     */
    public Text(String text, float x, float y, double rotate, int align) {
        this(text, x, y, rotate, align, null, 10, BaseColor.BLACK);
    }

    /**
     * Draw some text on the document
     *
     * @param text
     * @param x
     * @param y
     * @param rotate
     * @param align
     * @param fontName
     * @param fontSize
     * @param color
     */
    public Text(String text, float x, float y, double rotate, int align, String fontName, int fontSize, BaseColor color) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.rotate = rotate;
        this.align = align;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.color = color;
    }

    /**
     * Draw some text on the document
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        if (bf == null) {
            bf = getFont(fontName);
        }

        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float xx = (x >= 0) ? x : rect.getWidth() + x;
        float yy = (y >= 0) ? y : rect.getHeight() + y;

        cb.beginText();
        cb.setColorFill(color);
        cb.setColorStroke(color);
        if (bf != null) {
            cb.setFontAndSize(bf, fontSize);
        }
        cb.showTextAlignedKerned(getAlign(align), text, xx, yy, (float) rotate);
        cb.endText();
    }

    private int getAlign(int align) {
        if (align == ALIGN_CENTER) {
            return PdfContentByte.ALIGN_CENTER;
        }
        if (align == ALIGN_RIGHT) {
            return PdfContentByte.ALIGN_RIGHT;
        }
        return PdfContentByte.ALIGN_LEFT;
    }

    private BaseFont getFont(String fontName) {
        try {
            if (fontName != null) {
                return BaseFont.createFont(fontName, BaseFont.WINANSI, true);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return getDefaultFont();
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
        return "Text{" + "text=" + text + "x=" + x + "y=" + y + "rotate=" + rotate + "align=" + align + "fontName=" + fontName + "fontSize="
                + fontSize + "color=" + color + '}';
    }
}
