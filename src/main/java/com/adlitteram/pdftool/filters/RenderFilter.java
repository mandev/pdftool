package com.adlitteram.pdftool.filters;

import com.adlitteram.pdftool.utils.CloseUtils;
import com.adlitteram.pdftool.utils.NumUtils;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;

public class RenderFilter extends AbstractPdfFilter {

    //
    public static final int FIRST_PAGE = 1;
    public static final int LAST_PAGE = 0;
    public static final int BEFORE_LAST_PAGE = -1;
    //
    protected ArrayList<RenderableObject> renders = new ArrayList<>();
    protected String destination;
    protected String pattern;
    protected int pageNumber;

    /**
     * A filter to draw the renderable filters on the selected pages
     *
     * @param pageNumber
     */
    public RenderFilter(int pageNumber) {
        this(null, pageNumber);
    }

    /**
     * A filter used to group renderable filters to draw on the selected pages
     *
     * @param pattern
     */
    public RenderFilter(String pattern) {
        this(null, pattern);
    }

    /**
     * A filter to draw the renderable filters on the selected pages
     *
     * @param destination
     * @param pageNumber
     */
    public RenderFilter(String destination, int pageNumber) {
        this.destination = destination;
        this.pageNumber = pageNumber;
        this.pattern = null;
    }

    /**
     * A filter to draw the renderable filters on the selected pages
     *
     * @param destination
     * @param pattern
     */
    public RenderFilter(String destination, String pattern) {
        this.destination = destination;
        this.pattern = pattern;
        this.pageNumber = 0;
    }

    /**
     *
     * @param ro
     */
    public void addRender(RenderableObject ro) {
        renders.add(ro);
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
            File outputFile = getOutputFile(destination, inputFile);
            File tmpFile = getTmpFile(inputFile, outputFile);
            fos = new FileOutputStream(tmpFile);

            reader = new PdfReader(inputFile.getPath());
            PdfStamper stamper = new PdfStamper(reader, fos);

            int pageCount = reader.getNumberOfPages();

            if (pattern != null && pattern.length() > 0) {
                List<Integer> pageList = getSelectedList(pattern, pageCount);
                if (pageList.isEmpty()) {
                    throw new RuntimeException("No pages are selected with pattern " + pattern);
                }

                for (Integer page : pageList) {
                    doFilter(reader, stamper, page);
                }
            }
            else {
                int pn = NumUtils.clamp(1, pageNumber > 0 ? pageNumber : pageCount + pageNumber, pageCount);
                doFilter(reader, stamper, pn);
            }

            stamper.close();
            reader.close();
            fos.close();

            if (deleteSource) {
                inputFile.delete();
            }
            Files.move(tmpFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // moveFile(tmpFile, outputFile);
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

    private void doFilter(PdfReader reader, PdfStamper stamper, int pn) {
        PdfContentByte cb = stamper.getOverContent(pn);
        for (RenderableObject ro : renders) {
            cb.saveState();
            ro.render(reader, cb, pn);
            cb.restoreState();
        }
    }
}
