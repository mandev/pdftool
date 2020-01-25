package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import org.apache.commons.io.IOUtils;

public class Impose extends AbstractPdfFilter {

    public static final int SIMPLE = 0;
    public static final int PAIRED = 1;
    public static final int DIVIDE = 2;

    protected String destination;
    protected float width;
    protected float height;
    protected int type = SIMPLE;

    protected ArrayList<PageSlot> pageSlots;
    protected ArrayList<PageMark> pageMarks;

    /**
     * Impose documents
     *
     * @param width
     * @param height
     * @param x0
     * @param y0
     */
    public Impose(float width, float height, float x0, float y0) {
        this(null, width, height, SIMPLE, x0, y0);
    }

    /**
     * Impose documents
     *
     * @param dst
     * @param width
     * @param height
     * @param x0
     * @param y0
     */
    public Impose(String dst, float width, float height, float x0, float y0) {
        this(dst, width, height, SIMPLE, x0, y0);
    }

    /**
     * Impose documents
     *
     * @param width
     * @param height
     * @param type
     * @param x0
     * @param y0
     */
    public Impose(float width, float height, int type, float x0, float y0) {
        this(null, width, height, type, x0, y0);
    }

    /**
     *
     * @param dst
     * @param width
     * @param height
     * @param type
     * @param x0
     * @param y0
     */
    public Impose(String dst, float width, float height, int type, float x0, float y0) {
        this(dst, width, height, type);
        pageSlots.add(new PageSlot(x0, y0));
    }

    /**
     * Impose documents
     *
     * @param width
     * @param height
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public Impose(float width, float height, float x0, float y0, float x1, float y1) {
        this(null, width, height, SIMPLE, x0, y0, x1, y1);
    }

    /**
     * Impose documents
     *
     * @param dst
     * @param width
     * @param height
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public Impose(String dst, float width, float height, float x0, float y0, float x1, float y1) {
        this(dst, width, height, SIMPLE, x0, y0, x1, y1);
    }

    /**
     * Impose documents
     *
     * @param width
     * @param height
     * @param type
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public Impose(float width, float height, int type, float x0, float y0, float x1, float y1) {
        this(null, width, height, type, x0, y0, x1, y1);
    }

    /**
     * Impose documents
     *
     * @param dst
     * @param width
     * @param height
     * @param type
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public Impose(String dst, float width, float height, int type, float x0, float y0, float x1, float y1) {
        this(dst, width, height, type);
        pageSlots.add(new PageSlot(x0, y0));
        pageSlots.add(new PageSlot(x1, y1));
    }

    /**
     * Impose documents
     *
     * @param width
     * @param height
     * @param type
     */
    public Impose(float width, float height, int type) {
        this(null, width, height, type);
    }

    /**
     * Impose documents
     *
     * @param dst
     * @param width
     * @param height
     * @param type
     */
    public Impose(String dst, float width, float height, int type) {
        this.destination = dst;
        this.width = width;
        this.height = height;
        this.type = type;
        this.pageSlots = new ArrayList<>();  // Initialize here because of xstream
        this.pageMarks = new ArrayList<>();  // Initialize here because of xstream
    }

    /**
     *
     * @param pageSlot
     */
    public void addPageSlot(PageSlot pageSlot) {
        pageSlots.add(pageSlot);
    }

    /**
     *
     * @param pageSlot
     * @return
     */
    public boolean removePageSlot(PageSlot pageSlot) {
        return pageSlots.remove(pageSlot);
    }

    /**
     *
     * @param pageMark
     */
    public void addPageMark(PageMark pageMark) {
        pageMarks.add(pageMark);
    }

    /**
     *
     * @param pageMark
     * @return
     */
    public boolean removePageMark(PageMark pageMark) {
        return pageMarks.remove(pageMark);
    }

    private ArrayList<Integer> getSelectedPages(int pages) {
        ArrayList<Integer> list = new ArrayList<>(pages);
        int p2 = pages / 2;
        int offset = pages % 2;

        if (type == SIMPLE) {
            // 1 2, 2 4, 5 6, 7 8 or 1, 2 3, 4 5, 6 7, 8 9
            for (int i = 1; i <= pages; i++) {
                list.add(Integer.valueOf(i));
            }
        }
        else if (type == PAIRED) {
            // 8 1, 2 7, 6 3, 4 5 or 1, 9 2, 3 8, 7 4, 5 6
            if (offset == 1) {
                list.add(Integer.valueOf(1));
            }

            for (int i = 1 + offset; i <= p2 + offset; i += 2) {
                list.add(Integer.valueOf(pages + 1 + offset - i));
                list.add(Integer.valueOf(i));
                list.add(Integer.valueOf(i + 1));
                list.add(Integer.valueOf(pages + offset - i));
            }
        }
        else if (type == DIVIDE) {
            // 1 5, 2 6, 3 7, 4 8 or 1, 2 6, 3 7, 4 8, 5 9
            if (offset == 1) {
                list.add(Integer.valueOf(1));
            }

            for (int i = 1 + offset; i <= p2 + offset; i++) {
                list.add(Integer.valueOf(i));
                list.add(Integer.valueOf(p2 + i));
            }
        }

        return list;
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
        Document document = null;

        try {
            BaseFont font = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);

            reader = new PdfReader(inputFile.getPath());
            ArrayList<Integer> selectedPages = getSelectedPages(reader.getNumberOfPages());
            reader.selectPages(selectedPages);
            int pages = reader.getNumberOfPages();

            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            document = new Document(new Rectangle((float) width, (float) height));
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.setPageEmpty(false);

            Map<String, String> metadata = reader.getInfo();
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                document.addHeader(entry.getKey(), entry.getValue());
            }

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            int offset = pages % 2;
            if (offset == 1) {
                document.newPage();

                PageSlot pageSlot = (PageSlot) pageSlots.get(0);
                PdfImportedPage page = writer.getImportedPage(reader, 1);
                int rotation = reader.getPageRotation(1);
                int folio = selectedPages.get(0).intValue();

                addPage(cb, rotation, page, pageSlot);
                addText(cb, font, folio, metadata, inputFile, pageSlot);
                addMark(cb, page, 0);
            }

            for (int i = 1 + offset; i <= pages; i += pageSlots.size()) {
                document.newPage();

                for (int j = 0; j < pageSlots.size(); j++) {
                    PageSlot pageSlot = (PageSlot) pageSlots.get(j);
                    PdfImportedPage page = writer.getImportedPage(reader, i + j);
                    int rotation = reader.getPageRotation(i + j);
                    int folio = selectedPages.get(i + j - 1).intValue();

                    addPage(cb, rotation, page, pageSlot);
                    addText(cb, font, folio, metadata, inputFile, pageSlot);
                    addMark(cb, page, j);
                }
            }

            document.close();
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
            CloseUtils.closeQuietly(document);
            IOUtils.closeQuietly(fos);
            CloseUtils.closeQuietly(reader);
        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    /**
     *
     * @param inputFiles
     * @return
     */
    @Override
    public File[] filter(File[] inputFiles) {

        if (inputFiles.length == 1) {
            return filter(inputFiles[0]);
        }

        Set outputFiles = new LinkedHashSet();
        FileOutputStream fos = null;
        Document document = null;

        PdfReader[] readers = new PdfReader[inputFiles.length];

        try {
            BaseFont font = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);

            int pages = 0;
            for (int i = 0; i < inputFiles.length; i++) {
                readers[i] = new PdfReader(inputFiles[i].getPath());
                pages = Math.max(readers[i].getNumberOfPages(), pages);
            }

            File outputFile = getOutputFile(destination, inputFiles[0]);
            File tmpFile = getTmpFile(inputFiles[0], outputFile);
            fos = new FileOutputStream(tmpFile);

            document = new Document(new Rectangle((float) width, (float) height));
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.setPageEmpty(false);

            Map<String, String>[] metadatas = new HashMap[readers.length];
            for (int j = 0; j < readers.length; j++) {
                metadatas[j] = readers[j].getInfo();
            }

            for (Map.Entry<String, String> entry : metadatas[0].entrySet()) {
                document.addHeader(entry.getKey(), entry.getValue());
            }

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            for (int i = 1; i <= pages; i++) {
                document.newPage();

                for (int j = 0; j < readers.length; j++) {
                    if (i > readers[j].getNumberOfPages()) {
                        continue;
                    }

                    PageSlot pageSlot = (PageSlot) pageSlots.get(j);
                    PdfImportedPage page = writer.getImportedPage(readers[j], i);
                    int rotation = readers[j].getPageRotation(i);

                    addPage(cb, rotation, page, pageSlot);
                    addText(cb, font, i, metadatas[j], inputFiles[j], pageSlot);
                    addMark(cb, page, j);
                }
            }

            document.close();
            fos.close();
            for (PdfReader reader : readers) {
                reader.close();
            }

            if (deleteSource) {
                for (File inputFile : inputFiles) {
                    inputFile.delete();
                }
            }

            moveFile(tmpFile, outputFile);
            outputFiles.add(outputFile);
        }
        catch (Exception e) {
            e.printStackTrace();
            CloseUtils.closeQuietly(document);
            IOUtils.closeQuietly(fos);
            for (PdfReader reader : readers) {
                CloseUtils.closeQuietly(reader);
            }

        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    private void addPage(PdfContentByte cb, int rotation, PdfImportedPage page, PageSlot pageSlot) {
        if (rotation == 90 || rotation == 270) {
            cb.addTemplate(page, 0, -1f, 1f, 0, pageSlot.getX(), pageSlot.getY());
        }
        else {
            cb.addTemplate(page, 1f, 0, 0, 1f, pageSlot.getX(), pageSlot.getY());
        }
    }

    private void addText(PdfContentByte cb, BaseFont font, int folio, Map<String, String> metadata, File file, PageSlot pageSlot) {
        if (pageSlot.isFolioted()) {
            cb.beginText();
            cb.setFontAndSize(font, pageSlot.getFontSize());
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, pageSlot.getFolio(folio, metadata, file), pageSlot.getFolioX(), pageSlot.getFolioY(), 0);
            cb.endText();
        }
    }

    private void addMark(PdfContentByte cb, PdfImportedPage page, int index) {

        if (index < pageMarks.size()) {
            PageMark pageMark = (PageMark) pageMarks.get(index);
            PageSlot pageSlot = (PageSlot) pageSlots.get(index);

            cb.saveState();
            cb.setColorStroke(pageMark.getColor());
            cb.setLineWidth(pageMark.getThickness());

            float x;
            float y;
            char c;

            x = pageSlot.getX() + pageMark.getX();
            y = pageSlot.getY() + pageMark.getY();
            c = pageMark.getPosition().charAt(0);

            if (c == '-' || c == '+') {
                cb.moveTo(x - pageMark.getOffset(), y);
                cb.lineTo(x - pageMark.getOffset() - pageMark.getSize(), y);
            }

            if (c == '|' || c == '+') {
                cb.moveTo(x, y - pageMark.getOffset());
                cb.lineTo(x, y - pageMark.getOffset() - pageMark.getSize());
            }

            x = pageSlot.getX() + pageMark.getX();
            y = pageSlot.getY() + page.getHeight() - pageMark.getY();
            c = pageMark.getPosition().charAt(1);

            if (c == '-' || c == '+') {
                cb.moveTo(x - pageMark.getOffset(), y);
                cb.lineTo(x - pageMark.getOffset() - pageMark.getSize(), y);
            }

            if (c == '|' || c == '+') {
                cb.moveTo(x, y + pageMark.getOffset());
                cb.lineTo(x, y + pageMark.getOffset() + pageMark.getSize());
            }

            x = pageSlot.getX() + page.getWidth() - pageMark.getX();
            y = pageSlot.getY() + page.getHeight() - pageMark.getY();
            c = pageMark.getPosition().charAt(2);

            if (c == '-' || c == '+') {
                cb.moveTo(x + pageMark.getOffset(), y);
                cb.lineTo(x + pageMark.getOffset() + pageMark.getSize(), y);
            }

            if (c == '|' || c == '+') {
                cb.moveTo(x, y + pageMark.getOffset());
                cb.lineTo(x, y + pageMark.getOffset() + pageMark.getSize());
            }

            x = pageSlot.getX() + page.getWidth() - pageMark.getX();
            y = pageSlot.getY() + pageMark.getY();
            c = pageMark.getPosition().charAt(3);

            if (c == '-' || c == '+') {
                cb.moveTo(x + pageMark.getOffset(), y);
                cb.lineTo(x + pageMark.getOffset() + pageMark.getSize(), y);
            }

            if (c == '|' || c == '+') {
                cb.moveTo(x, y - pageMark.getOffset());
                cb.lineTo(x, y - pageMark.getOffset() - pageMark.getSize());
            }

            cb.stroke();
            cb.restoreState();
        }
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\ndestination: ");
        toStringBuilder.append(destination);
        toStringBuilder.append("\nwidth: ");
        toStringBuilder.append(width);
        toStringBuilder.append("\nheight: ");
        toStringBuilder.append(height);
        toStringBuilder.append("\ntype: ");
        toStringBuilder.append(type);
        toStringBuilder.append("\npageSlots: ");
        if (pageSlots != null) {
            toStringBuilder.append("\nSize: ");
            toStringBuilder.append(pageSlots.size());
            java.util.Iterator collectionIiterator = pageSlots.iterator();
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
        toStringBuilder.append("\npageMarks: ");
        if (pageMarks != null) {
            toStringBuilder.append("\nSize: ");
            toStringBuilder.append(pageMarks.size());
            java.util.Iterator collectionIiterator = pageMarks.iterator();
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
