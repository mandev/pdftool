package com.adlitteram.pdftool.filters;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class Create extends AbstractPdfFilter {
    
    protected String destination;
    protected float width;
    protected float height;
    protected int numPage;
    protected Properties properties;

    /**
     * Create a new document
     *
     * @param width
     * @param height
     * @param numPage
     */
    public Create(float width, float height, int numPage) {
        this(null, width, height, numPage, null);
    }

    /**
     * Create a new document
     *
     * @param destination
     * @param width
     * @param height
     * @param numPage
     */
    public Create(String destination, float width, float height, int numPage) {
        this(destination, width, height, numPage, null);
    }

    /**
     * Create a new document
     *
     * @param destination
     * @param width
     * @param height
     * @param numPage
     * @param properties
     */
    public Create(String destination, float width, float height, int numPage, Properties properties) {
        this.destination = destination;
        this.width = width;
        this.height = height;
        this.numPage = numPage;
        this.properties = properties;
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

        try {
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            Document document = new Document(new com.itextpdf.text.Rectangle(width, height));
            PdfWriter writer = PdfWriter.getInstance(document, fos);

            if (properties != null) {
                for (Map.Entry entry : properties.entrySet()) {
                    document.addHeader((String) entry.getKey(), (String) entry.getValue());
                }
            }

            document.open();
            document.newPage();
            writer.setPageEmpty(false);
            for (int i = 1; i < numPage; i++) {
                document.newPage();
                writer.setPageEmpty(false);
            }
            document.close();
            writer.close();
            fos.close();

            moveFile(tmpFile, outputFile);
            outputFiles.add(outputFile);
        }
        catch (Exception e) {
            e.printStackTrace();
            IOUtils.closeQuietly(fos);
        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    @Override
    public String toString() {
        return "Create{" + "width=" + width + "height=" + height + "numPage=" + numPage + '}';
    }
}
