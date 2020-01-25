package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import org.apache.commons.io.IOUtils;

public class AddMargin extends AbstractPdfFilter {

    protected String destination;
    protected float left;
    protected float top;
    protected float bottom;
    protected float right;

    /**
     * Add a margin around each page of the document.
     *
     * @param left
     * @param top
     * @param bottom
     * @param right
     */
    public AddMargin(float left, float top, float bottom, float right) {
        this(null, left, top, bottom, right);
    }

    /**
     * Add a margin around each page of the document.
     *
     * @param dst
     * @param left
     * @param top
     * @param bottom
     * @param right
     */
    public AddMargin(String dst, float left, float top, float bottom, float right) {
        this.destination = dst;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        Set outputFiles = new LinkedHashSet();
        PdfReader reader = null;
        Document document = null;
        FileOutputStream fos = null;

        try {
            reader = new PdfReader(inputFile.getPath());
            HashMap infoMap = reader.getInfo();
            int lastPage = reader.getNumberOfPages();

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            Rectangle rect = reader.getPageSizeWithRotation(1);
            document = new Document(new Rectangle(rect.getWidth() + left + right, rect.getHeight() + top + bottom));
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            Iterator it = infoMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                document.addHeader((String) entry.getKey(), (String) entry.getValue());
            }

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            for (int i = 1; i <= lastPage; i++) {
                rect = reader.getPageSizeWithRotation(i);
                document.setPageSize(new Rectangle(rect.getWidth() + left + right, rect.getHeight() + top + bottom));
                document.newPage();
                PdfImportedPage page = writer.getImportedPage(reader, i);
                int rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270) {
                    cb.addTemplate(page, 0, -1f, 1f, 0, left, rect.getHeight() + top);
                }
                else {
                    cb.addTemplate(page, 1f, 0, 0, 1f, left, bottom);
                }
            }

            document.close();
            reader.close();

            if (deleteSource) {
                inputFile.delete();
            }
            moveFile(tmpFile, outputFile);
            outputFiles.add(outputFile);
        }
        catch (Exception e) {
            e.printStackTrace();
            CloseUtils.closeQuietly(document);
            IOUtils.closeQuietly(fos);
            CloseUtils.closeQuietly(reader);
        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\nleft: ");
        toStringBuilder.append(left);
        toStringBuilder.append("\ntop: ");
        toStringBuilder.append(top);
        toStringBuilder.append("\nbottom: ");
        toStringBuilder.append(bottom);
        toStringBuilder.append("\nright: ");
        toStringBuilder.append(right);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
