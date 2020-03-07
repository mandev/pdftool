package com.adlitteram.pdftool.filters;

import com.itextpdf.text.pdf.SequenceList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public abstract class AbstractPdfFilter implements PdfFilter {

    protected boolean deleteSource;

    @Override
    public File[] filter(File[] srcFiles) {

        Set<File> outputFiles = new LinkedHashSet<>();
        if (srcFiles == null || srcFiles.length == 0) {
            File[] files = filter((File) null);
            outputFiles.addAll(Arrays.asList(files));
        }
        else {
            for (File srcFile : srcFiles) {
                File[] files = filter(srcFile);
                outputFiles.addAll(Arrays.asList(files));
            }
        }
        return outputFiles.toArray(new File[outputFiles.size()]);
    }

    @Override
    abstract public File[] filter(File srcFile);

    /**
     * @return the deleteSource
     */
    public boolean isDeleteSource() {
        return deleteSource;
    }

    /**
     * @param deleteSource the deleteSource to set
     */
    public void setDeleteSource(boolean deleteSource) {
        this.deleteSource = deleteSource;
    }

    /**
     *
     * @param dst
     * @param inputFile
     * @return
     */
    public static File getOutputFile(String dst, File inputFile) {
        return getOutputFile(dst, inputFile, "");
    }

    /**
     *
     * @param dst
     * @param inputFile
     * @param count
     * @return
     */
    public static File getOutputFile(String dst, File inputFile, String count) {
        if (dst == null) {
            return inputFile;
        }

        if (inputFile != null) {
            dst = dst.replace("{DIR}", inputFile.getParent());
            dst = dst.replace("{BASE}", FilenameUtils.getBaseName(inputFile.getName()));
            dst = dst.replace("{EXT}", FilenameUtils.getExtension(inputFile.getName()));
        }

        dst = dst.replace("{TIME}", String.valueOf(System.currentTimeMillis()));
        dst = dst.replace("{COUNT}", count);

        File file = null;

        if (dst.contains("{TEMP}")) {
            try {
                String dir = FilenameUtils.getFullPath(dst);
                File dirFile = ("".equals(dir)) ? null : new File(dir);

                String name = FilenameUtils.getName(dst);
                String[] fix = name.split("\\{TEMP\\}", 2);
                if (fix[0].length() < 3) {
                    fix[0] = "tmp_";
                }
                if (fix[1].length() == 0) {
                    fix[1] = ".tmp";
                }

                file = File.createTempFile(fix[0], fix[1], dirFile);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            file = new File(dst);
            if (!file.isAbsolute() && inputFile != null) {
                file = new File(inputFile.getParentFile(), file.getPath());
            }
        }
        return file;

    }

    /**
     *
     * @param inputFile
     * @param outputFile
     * @return
     * @throws IOException
     */
    public static File getTmpFile(File inputFile, File outputFile) throws IOException {
        if (inputFile != null && inputFile.equals(outputFile)) {
            File tmpFile = File.createTempFile("tmp_", ".pdf");
            //tmpFile.deleteOnExit();
            return tmpFile;
        }
        return outputFile;
    }

    /**
     *
     * @param srcFile
     * @param dstFile
     * @throws IOException
     */
    public static void moveFile(File srcFile, File dstFile) throws IOException {
        if (!srcFile.getCanonicalPath().equals(dstFile.getCanonicalPath())) {
            if (dstFile.exists()) {
                dstFile.delete();
            }
            FileUtils.moveFile(srcFile, dstFile);
        }
    }

    /**
     * Select a range of pages. The syntax is: [!][o][odd][e][even]start-end You can have multiple ranges separated by
     * commas ','. The '!' modifier removes the range from what is already selected. The range changes are incremental,
     * that is, numbers are added or deleted as the range appears. The start or the end, but not both, can be omitted.
     *
     * @param pattern
     * @param pages
     * @return
     */
    public List<Integer> getSelectedList(String pattern, int pages) {

        if (pattern.startsWith("~")) {
            List list = SequenceList.expand(pattern.substring(1), pages);
            Collections.sort(list);
            ArrayList<Integer> selectedList = new ArrayList<>(pages - list.size());
            for (int i = 1; i <= pages; i++) {
                if (Collections.binarySearch(list, i) < 0) {
                    selectedList.add(i);
                }
            }
            return selectedList;
        }
        return SequenceList.expand(pattern, pages);
    }
}
