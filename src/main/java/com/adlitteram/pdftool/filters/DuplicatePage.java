package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class DuplicatePage extends AbstractPdfFilter {

    protected String destination;
    protected int count;
    protected String pattern;

    /**
     * Duplicate the selected pages of the document
     *
     * Select a range of pages. The syntax is: [!][o][odd][e][even]start-end You
     * can have multiple ranges separated by commas ','. The '!' modifier
     * removes the range from what is already selected. The range changes are
     * incremental, that is, numbers are added or deleted as the range appears.
     * The start or the end, but not both, can be omitted.
     *
     * @param pattern the string used to define the selected pages
     */
    public DuplicatePage(String pattern) {
        this(null, pattern, 1);
    }

    /**
     * Duplicate the selected pages of the document
     *
     * @param pattern
     * @param count
     */
    public DuplicatePage(String pattern, int count) {
        this(null, pattern, count);
    }

    /**
     * Duplicate the selected pages of the document
     *
     * @param dst
     * @param pattern
     * @param count
     */
    public DuplicatePage(String dst, String pattern, int count) {
        this.destination = dst;
        this.pattern = pattern;
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

        try {
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath());
            int pages = reader.getNumberOfPages();

            PdfStamper stamper = new PdfStamper(reader, fos);

            List<Integer> list = getSelectedList(pattern, pages);
            if (list.isEmpty()) {
                throw new RuntimeException("No pages are selected with pattern " + pattern);
            }

            for (int j = 0; j < count; j++) {
                for (Integer value : list) {
                    pages++;
                    stamper.insertPage(pages, reader.getPageSizeWithRotation(value));
                    stamper.replacePage(reader, value, pages);
                }
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
        toStringBuilder.append("\n");
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\ncount: ");
        toStringBuilder.append(count);
        toStringBuilder.append("\npattern: ");
        toStringBuilder.append(pattern);
        return toStringBuilder.toString();
    }
}
