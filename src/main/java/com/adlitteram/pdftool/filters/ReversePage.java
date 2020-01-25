package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class ReversePage extends AbstractPdfFilter {

    protected String destination;

    /**
     * Reverse all pages
     *
     */
    public ReversePage() {
        this((String) null);
    }

    /**
     * Reverse all pages
     *
     * @param dst
     */
    public ReversePage(String dst) {
        this.destination = dst;
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

            int pages = reader.getNumberOfPages();
            ArrayList<Integer> list = new ArrayList<>(pages);
            for (int i = pages; i > 0; i--) {
                list.add(Integer.valueOf(i));
            }

            reader.selectPages(list);
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, fos);
            document.open();

            for (int i = 1; i <= pages; i++) {
                copy.addPage(copy.getImportedPage(reader, i));
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
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
