package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Concat extends AbstractPdfFilter {
    //

    protected String destination;

    /**
     * Concat documents into one file
     *
     */
    public Concat() {
        this(null);
    }

    /**
     * Concat documents into one file
     *
     * @param dst
     */
    public Concat(String dst) {
        this.destination = dst;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        File outputFile = getOutputFile(destination, inputFile);

        try {
            if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                FileUtils.copyFile(inputFile, outputFile);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        return new File[]{outputFile};
    }

    /**
     *
     * @param inputFiles
     * @return
     */
    @Override
    public File[] filter(File[] inputFiles) {
        Set outputFiles = new LinkedHashSet();
        FileOutputStream fos = null;
        PdfCopy writer = null;
        PdfReader reader = null;
        Document document = null;

        try {
            File outputFile = getOutputFile(destination, inputFiles[0]);
            File tmpFile = getTmpFile(inputFiles[0], outputFile);
            fos = new FileOutputStream(tmpFile);

            for (File inputFile : inputFiles) {
                reader = new PdfReader(inputFile.getPath());
                int np = reader.getNumberOfPages();
                if (np > 0 && writer == null) {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, fos);
                    document.open();
                }
                if (writer != null) {
                    for (int j = 1; j <= np; j++) {
                        writer.addPage(writer.getImportedPage(reader, j));
                    }
                }
                reader.close();
                if (deleteSource) {
                    inputFile.delete();
                }
            }

            if (document != null) {
                document.close();
            }
            fos.close();

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
