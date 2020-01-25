package com.adlitteram.pdftool.filters;

import java.io.File;
import java.util.ArrayList;

public class GroupFilter extends AbstractPdfFilter {

    protected ArrayList<PdfFilter> filters = new ArrayList<>();
    protected int count;

    /**
     * Group the filters in an independant execution loop
     *
     */
    public GroupFilter() {
        this(0);
    }

    /**
     * Group the filters in an independant execution loop
     *
     * @param count the number of loop to repeat
     */
    public GroupFilter(int count) {
        this.count = count;
    }

    /**
     * Remove all filters
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
     * @param srcFiles
     * @return
     */
    @Override
    public File[] filter(File[] srcFiles) {
        for (int j = 0; j < count; j++) {
            File[] files = srcFiles;
            for (PdfFilter filter : filters) {
                files = filter.filter(files);
            }
        }

        if (deleteSource) {
            for (File file : srcFiles) {
                file.delete();
            }
        }

        return srcFiles;
    }

    /**
     *
     * @param srcFile
     * @return
     */
    @Override
    public File[] filter(File srcFile) {
        return filter(new File[]{srcFile});
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ncount: ");
        toStringBuilder.append(count);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
