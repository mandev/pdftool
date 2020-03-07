package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.xml.XMLTools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PdfTool {

    protected ArrayList<PdfFilter> filters = new ArrayList<>();

    public PdfTool() {
    }

    /**
     *
     * @param file
     * @return
     */
    public static PdfTool createFromFile(File file) {
        try {
            return (PdfTool) XMLTools.decodeFromFile(file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param file
     */
    public void saveToFile(File file) {
        try {
            XMLTools.encodeToFile(this, file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public void reset() {
        filters.clear();
    }

    /**
     *
     * @param filter
     */
    public void addFilter(PdfFilter filter) {
        filters.add(filter);
    }

    /**
     *
     * @param filter
     * @return
     */
    public boolean removeFilter(PdfFilter filter) {
        return filters.remove(filter);
    }

    /**
     *
     */
    public void execute() {
        execute((File[]) null);
    }


    /**
     *
     * @param files
     */
    public void execute(File... files) {
        for (PdfFilter filter : filters) {
            files = filter.filter(files);
        }
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\nfilters: ");
        if (filters != null) {
            toStringBuilder.append("\nSize: ");
            toStringBuilder.append(filters.size());
            java.util.Iterator collectionIiterator = filters.iterator();
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
