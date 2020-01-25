package com.adlitteram.pdftool.filters;

import static com.adlitteram.pdftool.filters.AbstractPdfFilter.getOutputFile;
import static com.adlitteram.pdftool.filters.AbstractPdfFilter.getTmpFile;
import static com.adlitteram.pdftool.filters.AbstractPdfFilter.moveFile;
import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

public class SplitDocument extends AbstractPdfFilter {

    protected String destination1;
    protected String destination2;
    protected String destination3;
    protected int pageBreak1;
    protected int pageBreak2;

    /**
     * Split the document in two documents
     *
     * @param dst1
     * @param dst2
     * @param pageBreak1
     */
    public SplitDocument(String dst1, String dst2, int pageBreak1) {
        this(dst1, dst2, null, pageBreak1, -1);
    }

    public SplitDocument(String dst1, String dst2, String dst3, int pageBreak1, int pageBreak2) {
        this.destination1 = dst1;
        this.destination2 = dst2;
        this.destination3 = dst3;
        this.pageBreak1 = pageBreak1;
        this.pageBreak2 = pageBreak2;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        Set outputFiles = new LinkedHashSet();
        PdfReader reader = null;
        Document document1 = null;
        Document document2 = null;

        try {
            reader = new PdfReader(inputFile.getPath());
            int maxPage = reader.getNumberOfPages();

            File outputFile1 = getOutputFile(destination1, inputFile);
            File tmpFile1 = getTmpFile(inputFile, outputFile1);
            FileOutputStream fos1 = new FileOutputStream(tmpFile1);

            document1 = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy writer1 = new PdfCopy(document1, fos1);
            document1.open();

            for (int i = 1; i <= pageBreak1 && i <= maxPage; i++) {
                writer1.addPage(writer1.getImportedPage(reader, i));
            }
            document1.close();
            moveFile(tmpFile1, outputFile1);
            outputFiles.add(outputFile1);

            // PRAcroForm form = reader.getAcroForm();
            // if (form != null) writer1.copyAcroForm(reader);
            File outputFile2 = getOutputFile(destination2, inputFile);
            File tmpFile2 = getTmpFile(inputFile, outputFile2);
            FileOutputStream fos2 = new FileOutputStream(tmpFile2);

            document2 = new Document(reader.getPageSizeWithRotation(1));
            PdfCopy writer2 = new PdfCopy(document2, fos2);
            document2.open();

            if (destination3 == null || pageBreak2 == -1) {
                pageBreak2 = maxPage;
            }
            for (int i = pageBreak1 + 1; i <= pageBreak2; i++) {
                writer2.addPage(writer2.getImportedPage(reader, i));
            }
            document2.close();
            moveFile(tmpFile2, outputFile2);
            outputFiles.add(outputFile2);

            if (destination3 != null && pageBreak2 != -1) {
                File outputFile3 = getOutputFile(destination3, inputFile);
                File tmpFile3 = getTmpFile(inputFile, outputFile3);
                FileOutputStream fos3 = new FileOutputStream(tmpFile3);

                Document document3 = new Document(reader.getPageSizeWithRotation(1));
                PdfCopy writer3 = new PdfCopy(document3, fos3);
                document3.open();

                for (int i = pageBreak2 + 1; i <= maxPage; i++) {
                    writer3.addPage(writer3.getImportedPage(reader, i));
                }
                document3.close();
                moveFile(tmpFile3, outputFile3);
                outputFiles.add(outputFile3);
            }

            // PRAcroForm form = reader.getAcroForm();
            // if (form != null) writer2.copyAcroForm(reader);
            reader.close();
            if (deleteSource) {
                inputFile.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            CloseUtils.closeQuietly(document1);
            CloseUtils.closeQuietly(document2);
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
        toStringBuilder.append("\ndestination1: ");
        toStringBuilder.append(destination1);
        toStringBuilder.append("\ndestination2: ");
        toStringBuilder.append(destination2);
        toStringBuilder.append("\npageBreak: ");
        toStringBuilder.append(pageBreak1);
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
