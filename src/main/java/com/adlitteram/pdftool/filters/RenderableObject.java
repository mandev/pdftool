package com.adlitteram.pdftool.filters;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;

public interface RenderableObject {
    /**
     *
     * @param reader
     * @param cb
     * @param page
     */
    public void render(PdfReader reader, PdfContentByte cb, int page);
}
