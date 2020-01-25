package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class InsertPage extends AbstractPdfFilter {

    public static final int FIRST_PAGE = 1;
    public static final int LAST_PAGE = 0;

    protected String destination;
    protected int pageCount;
    protected int insertBefore;

    /**
     * Insert a new page in the document
     *
     * @param pageCount
     */
    public InsertPage(int pageCount) {
        this(null, pageCount, LAST_PAGE);
    }

    /**
     * Insert a new page in the document
     *
     * @param dst
     * @param pageCount
     */
    public InsertPage(String dst, int pageCount) {
        this(dst, pageCount, LAST_PAGE);
    }

    /**
     * Insert a new page in the document
     *
     * @param pageCount
     * @param insertPage
     */
    public InsertPage(int pageCount, int insertPage) {
        this(null, pageCount, insertPage);
    }

    /**
     * Insert a new page in the document
     *
     * @param dst
     * @param pageCount
     * @param insertPage
     */
    public InsertPage(String dst, int pageCount, int insertPage) {
        this.destination = dst;
        this.insertBefore = insertPage;
        this.pageCount = pageCount;
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
            int maxPage = reader.getNumberOfPages();

            PdfStamper stamper = new PdfStamper(reader, fos);
            int ipage = (insertBefore == 0) ? maxPage + 1 : insertBefore;
            Rectangle rect = reader.getPageSizeWithRotation(Math.min(ipage, maxPage));
            for (int i = 0; i < pageCount; i++) {
                stamper.insertPage(ipage, rect);
            }

            stamper.close();
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
        toStringBuilder.append("\npageCount: ");
        toStringBuilder.append(pageCount);
        toStringBuilder.append("\ninsertPage: ");
        toStringBuilder.append(insertBefore);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
