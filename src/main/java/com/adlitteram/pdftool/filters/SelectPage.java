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

public class SelectPage extends AbstractPdfFilter {

    protected String destination;
    protected String pattern;

    /**
     * Get the selected pages
     *
     * Select a range of pages. The syntax is: [!][o][odd][e][even]start-end You
     * can have multiple ranges separated by commas ','. The '!' modifier
     * removes the range from what is already selected. The range changes are
     * incremental, that is, numbers are added or deleted as the range appears.
     * The start or the end, but not both, can be omitted.
     * 
     * @param pattern
     */
    public SelectPage(String pattern) {
        this(null, pattern);
    }

    /**
     * Get the selected pages
     *
     * @param dst
     * @param pattern
     */
    public SelectPage(String dst, String pattern) {
        this.destination = dst;
        this.pattern = pattern;
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
            reader.selectPages(getSelectedList(pattern, reader.getNumberOfPages()));
            int pages = reader.getNumberOfPages();
            if (pages == 0) {
                throw new RuntimeException("No pages are selected with pattern " + pattern);
            }

            Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy writer = new PdfCopy(document, fos);
            document.open();

            for (int i = 1; i <= pages; i++) {
                writer.addPage(writer.getImportedPage(reader, i));
            }

//            PRAcroForm form = reader.getAcroForm();
//            if (form != null) {
//                writer.copyAcroForm(reader);
//            }
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
        toStringBuilder.append("\npattern: ");
        toStringBuilder.append(pattern);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
