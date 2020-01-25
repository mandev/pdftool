package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map.Entry;
import java.util.*;
import org.apache.commons.io.IOUtils;

public class AddMetadata extends AbstractPdfFilter {

    protected String destination;
    protected ArrayList<Metadata> metadatas = new ArrayList<>();   // For xstream serialization

    /**
     * Add Metadata to the document. Default Set is empty
     *
     */
    public AddMetadata() {
        this((String) null);
    }

    /**
     * Add Metadata to the document. Default Set is empty
     *
     * @param dst
     */
    public AddMetadata(String dst) {
        destination = dst;
    }

    /**
     * Add Metadata to the document
     *
     * @param properties store the metadatas (key, value)
     */
    public AddMetadata(Properties properties) {
        this(null, properties);
    }

    /**
     * Create an instance with some metadatas
     *
     * @param dst
     * @param properties
     */
    public AddMetadata(String dst, Properties properties) {
        destination = dst;
        addMetadata(properties);
    }

    /**
     * Add a metadata
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public AddMetadata(String key, String value) {
        this(null, key, value);
    }

    /**
     *
     * @param dst
     * @param key
     * @param value
     */
    public AddMetadata(String dst, String key, String value) {
        this.destination = dst;
        metadatas.add(new Metadata(key, value));
    }

    /**
     *
     * @param properties
     */
    public void addMetadata(Properties properties) {
        for (Entry entry : properties.entrySet()) {
            metadatas.add(new Metadata((String) entry.getKey(), (String) entry.getValue()));
        }
    }

    /**
     *
     * @param key
     * @param value
     */
    public void addMetadata(String key, String value) {
        metadatas.add(new Metadata(key, value));
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    public Object removeMetadata(String key, String value) {
        return metadatas.remove(new Metadata(key, value));
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
        PdfStamper stamper = null;

        try {
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath());
            stamper = new PdfStamper(reader, fos);

            HashMap<String, String> map = new HashMap<>();
            for (Metadata metadata : metadatas) {
                map.put(metadata.getKey(), metadata.getValue());
            }

            stamper.setMoreInfo(map);

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
            try {
                if (stamper != null) {
                    stamper.close();
                }
            }
            catch (Exception ex) {
            }
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
        toStringBuilder.append("\nmetadatas: ");
        if (metadatas != null) {
            toStringBuilder.append("\nSize: ");
            toStringBuilder.append(metadatas.size());
            java.util.Iterator collectionIiterator = metadatas.iterator();
            for (int i = 0; collectionIiterator.hasNext(); ++i) {
                toStringBuilder.append("\nIndex ");
                toStringBuilder.append(i);
                toStringBuilder.append(": ");
                toStringBuilder.append(collectionIiterator.next());
            }
        }
        else {
            toStringBuilder.append("NULL");
        }
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
