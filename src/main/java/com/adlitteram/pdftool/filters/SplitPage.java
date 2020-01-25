package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class SplitPage extends AbstractPdfFilter {
    //

    protected String destination;
    protected int pageStep;
    protected String fmt ;

    /**
     * Create multiple new documents with the pages of the document
     *
     * @param dst
     * @param pageStep
     */
    public SplitPage(String dst, int pageStep) {
        this(dst, pageStep, "%d") ;
    }

    public SplitPage(String dst, int pageStep, String fmt) {
        this.destination = dst;
        this.pageStep = pageStep;
        this.fmt = fmt;
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

        try {
            reader = new PdfReader(inputFile.getPath());
            int maxPage = reader.getNumberOfPages();

            for (int i = 1; i <= maxPage; i += pageStep) {

                File outputFile = getOutputFile(destination, inputFile, String.format(fmt, i));
                document = new Document(reader.getPageSizeWithRotation(1));

                PdfWriter writer = PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream(outputFile)));
                document.open();
                PdfContentByte cb = writer.getDirectContent();

                for (int j = 0; j < pageStep && i + j <= maxPage; j++) {
                    document.setPageSize(reader.getPageSizeWithRotation(i + j));
                    document.newPage();
                    PdfImportedPage page = writer.getImportedPage(reader, i + j);
                    int rotation = reader.getPageRotation(i + j);
                    if (rotation == 90 || rotation == 270) {
                        cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i + j).getHeight());
                    }
                    else {
                        cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                    }
                }

                document.close();
                outputFiles.add(outputFile);
            }

            if (deleteSource) {
                inputFile.delete();
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            CloseUtils.closeQuietly(document);
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
        toStringBuilder.append("\npageStep: ");
        toStringBuilder.append(pageStep);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
