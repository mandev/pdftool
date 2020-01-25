package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import org.apache.commons.io.IOUtils;

public class Resize extends AbstractPdfFilter {

    //
    protected String destination;
    protected float width1;
    protected float height1;
    protected float width;
    protected float height;

    /**
     * Change the width and the height of the document
     *
     * @param w
     * @param h
     */
    public Resize(float w, float h) {
        this(null, w, h, w, h);
    }

    /**
     * Change the width and the height of each page of the document
     *
     * @param dst
     * @param w
     * @param h
     */
    public Resize(String dst, float w, float h) {
        this(dst, w, h, w, h);
    }

    /**
     * Change the width and the height of each page of the document
     *
     * @param w1 first page width
     * @param h1 first page height
     * @param w
     * @param h
     */
    public Resize(float w1, float h1, float w, float h) {
        this(null, w1, h1, w, h);
    }

    /**
     * Change the width and the height of each page of the document
     *
     * @param dst
     * @param w1 first page width
     * @param h1 first page height
     * @param w
     * @param h
     */
    public Resize(String dst, float w1, float h1, float w, float h) {
        this.destination = dst;
        this.width1 = w1;
        this.height1 = h1;
        this.width = w;
        this.height = h;
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

        Rectangle rect1 = new Rectangle((float) width1, (float) height1);
        Rectangle rect2 = new Rectangle((float) width, (float) height);

        try {
            reader = new PdfReader(inputFile.getPath());
            int lastPage = reader.getNumberOfPages();
            HashMap info = reader.getInfo();

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            Rectangle rect = reader.getPageSizeWithRotation(1);
            AffineTransform af = AffineTransform.getScaleInstance(width1 / (double) rect.getWidth(), height1 / (double) rect.getHeight());
            double[] mx = new double[6];
            af.getMatrix(mx);

            document = new Document(rect1);
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            Iterator it = info.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                document.addHeader((String) entry.getKey(), (String) entry.getValue());
            }

            document.open();
            PdfContentByte cb = writer.getDirectContent();
            cb.addTemplate(writer.getImportedPage(reader, 1), (float) mx[0], (float) mx[1], (float) mx[2], (float) mx[3], (float) mx[4], (float) mx[5]);

            for (int i = 2; i <= lastPage; i++) {
                rect = reader.getPageSizeWithRotation(i);
                af = AffineTransform.getScaleInstance(width / (double) rect.getWidth(), height / (double) rect.getHeight());
                af.getMatrix(mx);
                document.setPageSize(rect2);
                document.newPage();
                cb.addTemplate(writer.getImportedPage(reader, i), (float) mx[0], (float) mx[1], (float) mx[2], (float) mx[3], (float) mx[4], (float) mx[5]);
            }

            document.close();
            fos.close();
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
        toStringBuilder.append("\nwidth1: ");
        toStringBuilder.append(width1);
        toStringBuilder.append("\nheight1: ");
        toStringBuilder.append(height1);
        toStringBuilder.append("\nwidth: ");
        toStringBuilder.append(width);
        toStringBuilder.append("\nheight: ");
        toStringBuilder.append(height);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
