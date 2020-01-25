package com.adlitteram.pdftool;

import java.util.Properties;
import java.util.Set;

public class PdfInfo {

    private final String pdfVersion;
    private final int pageCount;
    private final int objectCount;
    private final long fileLength;
    private final boolean isEncrypted;
    private final String permissions;
    private final boolean is128bits;
    private final boolean isRebuilt;
    private final Dimension.Float firstPageSize;
    private final Dimension.Float lastPageSize;
    private final Properties properties;
    private final byte[] xmlMetadata;

    public PdfInfo(String pdfVersion, int pageCount, int objectCount, long fileLength, boolean isEncrypted, String permissions, boolean is128bits,
            boolean isRebuilt, Properties properties, byte[] xmlMetadata,
            Dimension.Float firstPageSize, Dimension.Float lastPageSize) {

        this.pdfVersion = pdfVersion;
        this.pageCount = pageCount;
        this.objectCount = objectCount;
        this.fileLength = fileLength;
        this.isEncrypted = isEncrypted;
        this.permissions = permissions;
        this.is128bits = is128bits;
        this.isRebuilt = isRebuilt;
        this.properties = properties;
        this.xmlMetadata = xmlMetadata;
        this.firstPageSize = firstPageSize;
        this.lastPageSize = lastPageSize;
    }

    /**
     *
     * @return the PDF Version of the document
     */
    public String getPdfVersion() {
        return pdfVersion;
    }

    /**
     *
     * @return the Number of Page in the document
     */
    public int getNumberOfPages() {
        return pageCount;
    }

    /**
     *
     * @return the number of objects in the document
     */
    public int getXrefSize() {
        return objectCount;
    }

    /**
     *
     * @return the file length in byte
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     *
     * @return true if the document is encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     *
     * @return true if the document is 128bits encrypted
     */
    public boolean is128bits() {
        return is128bits;
    }

    /**
     *
     * @return true if the document has been rebuilt
     */
    public boolean isRebuilt() {
        return isRebuilt;
    }

    /**
     *
     * @return the permissions
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     *
     * @param key
     * @return the value of the metadata
     */
    public String getMetadata(String key) {
        return (String) properties.get(key);
    }

    /**
     *
     * @return all metadatas
     */
    public Properties getProperties() {
        return (Properties) properties.clone();
    }

    /**
     *
     * @return the Set of metadata
     */
    public Set getEntrySet() {
        return properties.entrySet();
    }

    /**
     *
     * @return the XML of metadata
     */
    public byte[] getXmlMetadata() {
        return xmlMetadata;
    }

    public Dimension.Float getFirstPageSize() {
        return firstPageSize;
    }

    public Dimension.Float getLastPageSize() {
        return lastPageSize;
    }
}
