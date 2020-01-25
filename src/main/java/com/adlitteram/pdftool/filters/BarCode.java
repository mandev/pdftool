package com.adlitteram.pdftool.filters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BarcodeInter25;
import com.itextpdf.text.pdf.BarcodePostnet;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import java.awt.geom.AffineTransform;

public class BarCode implements RenderableObject {

    public static int ALIGN_LEFT = 0;
    public static int ALIGN_RIGHT = 1;
    public static int ALIGN_CENTER = 2;
    //
    public static final String BARCODE_128 = "128";
    public static final String BARCODE_39 = "39";
    public static final String BARCODE_EAN = "ean";
    public static final String BARCODE_EANSUPP = "eansupp";
    public static final String BARCODE_INTER25 = "inter25";
    public static final String BARCODE_POSTNET = "postnet";
    //
    protected String code;
    protected int fontSize;
    protected float x;
    protected float y;
    protected float sx;
    protected float sy;
    protected double rotate;
    protected int align;
    protected String type;
    protected BaseColor barColor;
    protected BaseColor txtColor;
    protected BaseColor bgColor;
    //
    protected Barcode barcode;

    /**
     * Draw a barcode
     *
     * @param code
     * @param x
     * @param y
     */
    public BarCode(String code, float x, float y) {
        this(code, x, y, 1f, 1f, 0d, 0);
    }

    /**
     * Draw a barcode
     *
     * @param code
     * @param x
     * @param y
     * @param sx
     * @param sy
     * @param rotate
     * @param align
     */
    public BarCode(String code, float x, float y, float sx, float sy, double rotate, int align) {
        this(code, x, y, sx, sy, rotate, align, 0, BaseColor.BLACK, BaseColor.BLACK, BaseColor.WHITE, BARCODE_128);
    }

    /**
     * Draw a barcode
     *
     * @param code
     * @param x
     * @param y
     * @param sx
     * @param sy
     * @param rotate
     * @param align
     * @param fontSize
     * @param barColor
     * @param txtColor
     * @param bgColor
     * @param type
     */
    public BarCode(String code, float x, float y, float sx, float sy, double rotate, int align, int fontSize, BaseColor barColor, BaseColor txtColor,
            BaseColor bgColor, String type) {
        this.code = code;
        this.fontSize = fontSize;
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.rotate = rotate;
        this.align = align;
        this.barColor = barColor;
        this.txtColor = txtColor;
        this.bgColor = bgColor;
        this.type = type;
    }

    /**
     *
     * @param type
     * @return
     */
    public static Barcode getBarcode(String type) {
        if (BARCODE_39.equalsIgnoreCase(type)) {
            return new Barcode39();
        }
        if (BARCODE_EAN.equalsIgnoreCase(type)) {
            return new BarcodeEAN();
        }
        if (BARCODE_INTER25.equalsIgnoreCase(type)) {
            return new BarcodeInter25();
        }
        if (BARCODE_POSTNET.equalsIgnoreCase(type)) {
            return new BarcodePostnet();
        }
        return new Barcode128();
    }

    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    @Override
    public void render(PdfReader reader, PdfContentByte cb, int page) {
        if (barcode == null) {
            barcode = getBarcode(type);
            barcode.setCode(code);
            if (fontSize > 0) {
                barcode.setSize(fontSize);
            }
            if (fontSize < 0) {
                barcode.setAltText("");
            }
        }

        com.itextpdf.text.Rectangle rect = reader.getPageSizeWithRotation(page);
        float xx = (x >= 0) ? x : rect.getWidth() + x;
        float yy = (y >= 0) ? y : rect.getHeight() + y;

        PdfTemplate t1 = barcode.createTemplateWithBarcode(cb, barColor, txtColor);

        PdfTemplate t2 = cb.createTemplate(t1.getWidth() + 10, t1.getHeight() + 10);

        t2.setColorFill(bgColor);
        t2.setColorStroke(bgColor);
        t2.rectangle(0, 0, t2.getWidth(), t2.getHeight());
        t2.fillStroke();
        t2.addTemplate(t1, 5, 5);

        AffineTransform af = new AffineTransform();
        af.rotate(Math.toRadians(rotate), t2.getWidth() / 2d, t2.getHeight() / 2d);
        af.translate(getAlignement(xx, t2.getWidth() * sx), yy);
        af.scale(sx, sy);

        double[] mx = new double[6];
        af.getMatrix(mx);
        cb.addTemplate(t2, (float) mx[0], (float) mx[1], (float) mx[2], (float) mx[3], (float) mx[4], (float) mx[5]);
    }

    private float getAlignement(float x, float width) {
        if (align == ALIGN_CENTER) {
            return x - width / 2f;
        }
        if (align == ALIGN_RIGHT) {
            return x - width;
        }
        return x;
    }

    @Override
    public String toString() {
        return "BarCode{" + "code=" + code + "fontSize=" + fontSize + "x=" + x + "y=" + y + "sx=" + sx + "sy=" + sy + "rotate=" + rotate + "align="
                + align + "type=" + type + "barColor=" + barColor + "txtColor=" + txtColor + '}';
    }

}
