package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class CropMark extends AbstractPdfFilter {

    //
    protected String destination;
    protected ArrayList marks;

    /**
     * Add a crop mark to the document
     *
     */
    public CropMark() {
        this(null);
    }

    /**
     * Add a crop mark to the document
     *
     * @param dst
     */
    public CropMark(String dst) {
        this.destination = dst;
        this.marks = new ArrayList();
    }

    /**
     *
     * @param lm
     */
    public void addLineMark(LineMark lm) {
        marks.add(lm);
    }

    /**
     *
     * @param lm
     * @return
     */
    public boolean removeLineMark(LineMark lm) {
        return marks.remove(lm);
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        Set outputFiles = new LinkedHashSet();
        FileOutputStream fos = null;
        PdfReader reader = null;

        try {
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath());
            PdfStamper stamp = new PdfStamper(reader, fos);

            int pagecount = reader.getNumberOfPages();
            for (int i = 1; i <= pagecount; i++) {
                PdfContentByte cb = stamp.getOverContent(i);
                for (int j = 0; j < marks.size(); j++) {
                    LineMark lm = (LineMark) marks.get(j);
                    cb.saveState();
                    cb.setColorStroke(lm.getColor());
                    cb.setLineWidth(lm.getThickness());
                    cb.moveTo(lm.getX1(), lm.getY1());
                    cb.lineTo(lm.getX2(), lm.getY2());
                    cb.stroke();
                    cb.restoreState();
                }
            }
            stamp.close();
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
        toStringBuilder.append("\nlineMarks: ");
        if (marks != null) {
            toStringBuilder.append("\nSize: ");
            toStringBuilder.append(marks.size());
            java.util.Iterator collectionIiterator = marks.iterator();
            for (int i = 0; collectionIiterator.hasNext(); ++i) {
                toStringBuilder.append("\nIndex ");
                toStringBuilder.append(i);
                toStringBuilder.append(": ");
                toStringBuilder.append(collectionIiterator.next());
            }
        }
        else {
            toStringBuilder.append("NULL");
        }
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
