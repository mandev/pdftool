package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class PaddingPage extends AbstractPdfFilter {

    public static final int FIRST_PAGE = 1;
    public static final int LAST_PAGE = 0;

    protected String destination;
    protected int insertBefore;
    protected int modulo;

    /**
     * Append some pages to the document
     *
     * @param padding
     */
    public PaddingPage(int padding) {
        this(null, padding, LAST_PAGE);
    }

    /**
     * Append some pages to the document
     *
     * @param dst
     * @param padding
     */
    public PaddingPage(String dst, int padding) {
        this(dst, padding, LAST_PAGE);
    }

    /**
     * Insert some pages to the document
     *
     * @param padding
     * @param insertPage
     */
    public PaddingPage(int padding, int insertPage) {
        this(null, padding, insertPage);
    }

    /**
     * Insert some pages to the document
     *
     * @param dst
     * @param padding
     * @param insertPage
     */
    public PaddingPage(String dst, int padding, int insertPage) {
        this.destination = dst;
        this.modulo = padding;
        this.insertBefore = insertPage;
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

            reader = new PdfReader(inputFile.getPath());
            int maxPage = reader.getNumberOfPages();
            int pageCount = maxPage % modulo;

            if (pageCount == 0) {
                if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                    FileUtils.copyFile(inputFile, outputFile);
                }
            }
            else {
                File tmpFile = getTmpFile(inputFile, outputFile);
                fos = new FileOutputStream(tmpFile);

                PdfStamper stamper = new PdfStamper(reader, fos);
                int ipage = (insertBefore == 0) ? maxPage + 1 : insertBefore;
                Rectangle rect = reader.getPageSizeWithRotation(Math.min(ipage, maxPage));
                for (int i = 0; i < pageCount; i++) {
                    stamper.insertPage(ipage, rect);
                }

                stamper.close();
                fos.close();

                if (deleteSource) {
                    inputFile.delete();
                }
                moveFile(tmpFile, outputFile);
            }

            reader.close();
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
        toStringBuilder.append("\ninsertPage: ");
        toStringBuilder.append(insertBefore);
        toStringBuilder.append("\npadding: ");
        toStringBuilder.append(modulo);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
