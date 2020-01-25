package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class Decrypt extends AbstractPdfFilter {

    protected String destination;
    protected String ownerPasswd;

    /**
     * Decrypt the encrypted document
     *
     * @param ownerPasswd
     */
    public Decrypt(String ownerPasswd) {
        this(null, ownerPasswd);
    }

    /**
     * Decrypt the encrypted document
     *
     * @param dst
     * @param ownerPasswd
     */
    public Decrypt(String dst, String ownerPasswd) {
        this.destination = dst;
        this.ownerPasswd = ownerPasswd;
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

            byte[] ownerpassword = (ownerPasswd == null) ? null : ownerPasswd.getBytes();

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath(), ownerpassword);
            new PdfStamper(reader, fos).close();

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
        toStringBuilder.append("\nownerPasswd: ");
        toStringBuilder.append(ownerPasswd);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
