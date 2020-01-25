
package com.adlitteram.pdftool.filters;

import java.io.File;
import java.util.Map;

public class PageSlot {

    protected float x;
    protected float y;
    protected boolean folio;
    protected float folioX;
    protected float folioY;
    protected String folioText;
    protected int folioSize;

    /**
     *
     * @param x
     * @param y
     */
    public PageSlot(float x, float y) {
        this(x, y, false, 0, 0, "", 10);
    }

    /**
     *
     * @param x
     * @param y
     * @param folio
     * @param folioX
     * @param folioY
     * @param folioText
     * @param fontSize
     */
    public PageSlot(float x, float y, boolean folio, float folioX, float folioY, String folioText, int fontSize) {
        this.x = x;
        this.y = y;
        this.folio = folio;
        this.folioX = folioX;
        this.folioY = folioY;
        this.folioText = folioText;
        this.folioSize = fontSize;
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
     * @return the printFolio
     */
    public boolean isFolioted() {
        return folio;
    }

    /**
     * @return the folioX
     */
    public float getFolioX() {
        return folioX;
    }

    /**
     * @return the folioY
     */
    public float getFolioY() {
        return folioY;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return folioSize;
    }

    /**
     * @return the text
     */
    public String getFolioText() {
        return folioText;
    }

    /**
     *
     * @param folio
     * @param metadata
     * @param file
     * @return
     */
    public String getFolio(int folio, Map<String, String> metadata, File file) {
        if (folioText == null || folioText.length() == 0)
            return String.valueOf(folio);

        String text = folioText.replace("{FOLIO}", String.valueOf(folio));
        text = text.replace("{FILENAME}", file.getName());
        text = text.replace("{FILEPATH}", file.getPath());

        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            text = text.replaceAll("\\{META:" + entry.getKey() + "\\}", entry.getValue());
        }

        return text;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\nx: ");
        toStringBuilder.append(x);
        toStringBuilder.append("\ny: ");
        toStringBuilder.append(y);
        toStringBuilder.append("\nfolioted: ");
        toStringBuilder.append(folio);
        toStringBuilder.append("\nfolioX: ");
        toStringBuilder.append(folioX);
        toStringBuilder.append("\nfolioY: ");
        toStringBuilder.append(folioY);
        toStringBuilder.append("\ntext: ");
        toStringBuilder.append(folioText);
        toStringBuilder.append("\nfontSize: ");
        toStringBuilder.append(folioSize);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
