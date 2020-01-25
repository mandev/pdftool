package com.adlitteram.pdftool;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PdfExtractor {

    public static PdfInfo getPdfInfo(File inputFile) {
        return getPdfInfo(inputFile, null);
    }

    public static PdfInfo getPdfInfo(File inputFile, String ownerPasswd) {

        Properties properties = new Properties();
        PdfReader reader = null;
        byte[] xmlMetadata = null;

        String pdfVersion = "";
        int pageCount = 0;
        int objectCount = 0;
        long fileLength = 0;
        boolean isEncrypted = false;
        String permissions = "";
        boolean is128bits = false;
        boolean isRebuilt = false;
        Dimension.Float firstPageSize = null;
        Dimension.Float lastPageSize = null;

        try {
            byte[] ownerpassword = (ownerPasswd == null) ? null : ownerPasswd.getBytes();
            reader = new PdfReader(inputFile.getPath(), (ownerpassword == null) ? null : ownerpassword);

            pdfVersion = String.valueOf(reader.getPdfVersion());
            pageCount = reader.getNumberOfPages();
            objectCount = reader.getXrefSize();
            fileLength = reader.getFileLength();
            isEncrypted = reader.isEncrypted();
            if (isEncrypted) {
                permissions = PdfEncryptor.getPermissionsVerbose((int) reader.getPermissions());
                is128bits = reader.is128Key();
            }
            isRebuilt = reader.isRebuilt();

            HashMap info = reader.getInfo();
            for (Iterator i = info.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                properties.setProperty(key, value);
            }

            if (reader.getMetadata() == null) {
                xmlMetadata = reader.getMetadata();
            }

            Rectangle rect = reader.getPageSizeWithRotation(1);
            firstPageSize = new Dimension.Float(rect.getWidth(), rect.getHeight());

            rect = reader.getPageSizeWithRotation(reader.getNumberOfPages());
            lastPageSize = new Dimension.Float(rect.getWidth(), rect.getHeight());

            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (reader != null) {
                reader.close();
            }
        }

        return new PdfInfo(pdfVersion, pageCount, objectCount, fileLength,
                isEncrypted, permissions, is128bits, isRebuilt, properties,
                xmlMetadata, firstPageSize, lastPageSize);
    }

    public static Dimension.Float getPageSize(File inputFile, int pageNumber) {
        return getPageSize(inputFile, pageNumber, null);
    }

    public static Dimension.Float getPageSize(File inputFile, int pageNumber, String ownerPasswd) {
        PdfReader reader = null;
        
        try { 
            byte[] ownerpassword = (ownerPasswd == null) ? null : ownerPasswd.getBytes();
            reader = new PdfReader(new RandomAccessFileOrArray(inputFile.getPath(), false, true), (ownerpassword == null) ? null : ownerpassword);
            pageNumber = Math.max(1, Math.min(pageNumber, reader.getNumberOfPages()));
            Rectangle rect = reader.getPageSizeWithRotation(pageNumber);
            reader.close();
            return new Dimension.Float(rect.getWidth(), rect.getHeight());
        }
        catch (Exception e) {
            e.printStackTrace();
            CloseUtils.closeQuietly(reader);
        }

        return null;
    }
}
