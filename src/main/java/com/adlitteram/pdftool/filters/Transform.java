package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class Transform extends AbstractPdfFilter {

    protected String destination;
    protected double scaleX;
    protected double scaleY;
    protected double rotate;

    /**
     * Rescale and rotate the document
     *
     * @param sx
     * @param sy
     * @param rotation
     */
    public Transform(double sx, double sy, double rotation) {
        this(null, sx, sy, rotation);
    }

    /**
     * Rescale and rotate the document
     *
     * @param dst
     * @param sx
     * @param sy
     * @param rotation
     */
    public Transform(String dst, double sx, double sy, double rotation) {
        this.destination = dst;
        this.scaleX = sx;
        this.scaleY = sy;
        this.rotate = rotation;
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

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            AffineTransform af = AffineTransform.getScaleInstance(scaleX, scaleY);
            af.rotate(rotate);

            Rectangle rect = reader.getPageSizeWithRotation(1);
            Rectangle2D.Float rectangle = new Rectangle2D.Float(0, 0, rect.getWidth(), rect.getHeight());
            Rectangle2D bounds = af.createTransformedShape(rectangle).getBounds2D();

            document = new Document();
            document.setPageSize(new Rectangle((float) bounds.getWidth(), (float) bounds.getHeight()));
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();

            PdfContentByte cb = writer.getDirectContent();
            int maxPage = reader.getNumberOfPages();

            for (int i = 1; i <= maxPage; i++) {
                rect = reader.getPageSizeWithRotation(i);
                rectangle = new Rectangle2D.Float(0, 0, rect.getWidth(), rect.getHeight());
                bounds = af.createTransformedShape(rectangle).getBounds2D();
                rect = new Rectangle((float) bounds.getWidth(), (float) bounds.getHeight());
                document.setPageSize(rect);
                document.newPage();

                AffineTransform af1 = AffineTransform.getTranslateInstance(-bounds.getX(), -bounds.getY());
                af1.scale(scaleX, scaleY);
                af1.rotate(Math.toRadians(rotate));

                double[] mx = new double[6];
                af1.getMatrix(mx);
                cb.addTemplate(writer.getImportedPage(reader, i), (float) mx[0], (float) mx[1], (float) mx[2], (float) mx[3], (float) mx[4], (float) mx[5]);
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
        toStringBuilder.append("\nscaleX: ");
        toStringBuilder.append(scaleX);
        toStringBuilder.append("\nscaleY: ");
        toStringBuilder.append(scaleY);
        toStringBuilder.append("\nrotate: ");
        toStringBuilder.append(rotate);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
