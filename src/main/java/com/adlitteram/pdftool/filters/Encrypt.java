package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class Encrypt extends AbstractPdfFilter {

    protected String destination;
    protected String ownerPasswd;
    protected String userPasswd;

    /**
     * Encrypt the document
     *
     * @param ownerPasswd
     * @param userPasswd
     */
    public Encrypt(String ownerPasswd, String userPasswd) {
        this(null, ownerPasswd, userPasswd);
    }

    /**
     * Encrypt the document
     *
     * @param dst
     * @param ownerPasswd
     * @param userPasswd
     */
    public Encrypt(String dst, String ownerPasswd, String userPasswd) {
        this.destination = dst;
        this.ownerPasswd = ownerPasswd;
        this.userPasswd = userPasswd;
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

            byte[] userpassword = (userPasswd == null) ? null : userPasswd.getBytes();
            byte[] ownerpassword = (ownerPasswd == null) ? null : ownerPasswd.getBytes();
            int permissions = 0;

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath());
            PdfEncryptor.encrypt(reader, fos, userpassword, ownerpassword, permissions, true);

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
        toStringBuilder.append("\nuserPasswd: ");
        toStringBuilder.append(userPasswd);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
