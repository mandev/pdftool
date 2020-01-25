package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class Merge extends AbstractPdfFilter {

    protected String destination;
    protected String pdfPath;
    protected int count;

    /**
     * Append another document to the document
     *
     * @param pdfPath
     * @param count
     */
    public Merge(String pdfPath, int count) {
        this(null, pdfPath, count);
    }

    /**
     * Append another document to the document
     *
     * @param dst
     * @param pdfPath
     * @param count
     */
    public Merge(String dst, String pdfPath, int count) {
        this.destination = dst;
        this.pdfPath = pdfPath;
        this.count = count;
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
        Document document = new Document();

        try {
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);

            fos = new FileOutputStream(tmpFile);
            PdfCopy copy = new PdfCopy(document, fos);

            reader = new PdfReader(inputFile.getPath());
            for (int j = 1; j <= reader.getNumberOfPages(); j++) {
                copy.addPage(copy.getImportedPage(reader, j));
            }
            reader.close();

            reader = new PdfReader(pdfPath);
            for (int i = 0; i < count; i++) {
                for (int j = 1; j <= reader.getNumberOfPages(); j++) {
                    copy.addPage(copy.getImportedPage(reader, j));
                }
            }
            reader.close();

            document.close();
            fos.close();

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
        return "Merge{" + "pdfPath=" + pdfPath + "count=" + count + '}';
    }
}
