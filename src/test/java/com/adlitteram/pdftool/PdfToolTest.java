/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adlitteram.pdftool;

import com.adlitteram.pdftool.filters.AddMargin;
import com.adlitteram.pdftool.filters.PdfTool;
import com.adlitteram.pdftool.filters.BarCode;
import com.adlitteram.pdftool.filters.Concat;
import com.adlitteram.pdftool.filters.Create;
import com.adlitteram.pdftool.filters.CropMargin;
import com.adlitteram.pdftool.filters.DuplicatePage;
import com.adlitteram.pdftool.filters.GroupFilter;
import com.adlitteram.pdftool.filters.Image;
import com.adlitteram.pdftool.filters.Impose;
import com.adlitteram.pdftool.filters.InsertPage;
import com.adlitteram.pdftool.filters.Multiply;
import com.adlitteram.pdftool.filters.PaddingPage;
import com.adlitteram.pdftool.filters.PageMark;
import com.adlitteram.pdftool.filters.PageSlot;
import com.adlitteram.pdftool.filters.Rectangle;
import com.adlitteram.pdftool.filters.RemovePage;
import com.adlitteram.pdftool.filters.RenderFilter;
import com.adlitteram.pdftool.filters.Resize;
import com.adlitteram.pdftool.filters.SelectPage;
import com.adlitteram.pdftool.filters.SplitDocument;
import com.adlitteram.pdftool.filters.SplitPage;
import com.adlitteram.pdftool.filters.Template;
import com.adlitteram.pdftool.filters.Text;
import com.adlitteram.pdftool.filters.Transform;
import com.adlitteram.pdftool.filters.WaterMark;
import com.adlitteram.pdftool.utils.NumUtils;
import com.itextpdf.text.BaseColor;
import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfToolTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfToolTest.class);

    public static final String TEST_RESOURCES = "src/test/resources/";
    public static final String TEST_RESULTS = "/tmp/";
    public static final double MMd = 72 / 25.4d;
    public static final float MMf = (float) MMd;

    public void testRenderPdf() {

        File ouputfile = new File(TEST_RESULTS + "testRender.pdf");

        RenderFilter rf = new RenderFilter(ouputfile.getPath(), "1, 4-8");
        rf.addRender(new BarCode("14567890111", 50 * MMf, 3 * MMf, 3f, 3f, 0, 0));
        rf.addRender(new WaterMark("coucou", 20, .5d));
        rf.addRender(new PageMark(100 * MMf, 100 * MMf, 10 * MMf, 3 * MMf, "+"));
        rf.addRender(new Rectangle(10 * MMf, 10 * MMf, 100 * MMf, 100 * MMf));
        rf.addRender(new Template(TEST_RESOURCES + "template.pdf", 1, -10 * MMf, -10 * MMf, 60 * MMf, 60 * MMf, 1, BaseColor.GRAY));

        File file = new File(TEST_RESOURCES + "A4.pdf");

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(rf);
        pdfTool.execute(file);

        Assertions.assertTrue(ouputfile.exists(), "Check if output exists");
        Assertions.assertTrue(ouputfile.length() > 10, "Check if output is not empty");
    }

    public void testCreatePdf() {
        File ouputfile = new File(TEST_RESULTS + "testCreate.pdf");

        RenderFilter rf = new RenderFilter("10");
        rf.addRender(new Text("Hello", 8 * MMf, -5 * MMf - 12f));
        rf.addRender(new Text("World", 8 * MMf, -5 * MMf - 12 * 2f));
        rf.addRender(new Rectangle(5 * MMf, 5 * MMf, -5 * MMf, -5 * MMf));
        rf.addRender(new BarCode("14567890111", 78 * MMf, 10 * MMf, 1f, 1f, 90, BarCode.ALIGN_CENTER));
        rf.addRender(new Image(TEST_RESOURCES + "image.jpg", 10 * MMf, 10 * MMf, 0, 0, 1, BaseColor.CYAN));

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(new Create(ouputfile.getPath(), 210 * MMf, 210 * MMf, 2));
        pdfTool.addFilter(rf);
        pdfTool.execute();

        Assertions.assertTrue(ouputfile.exists(), "Check if output exists");
        Assertions.assertTrue(ouputfile.length() > 10, "Check if output is not empty");
    }

    public void testCropPdf() {
        File ouputfile1 = new File(TEST_RESULTS + "testCrop_left.pdf");
        File ouputfile2 = new File(TEST_RESULTS + "testCrop_right.pdf");

        File file = new File(TEST_RESOURCES + "A2.pdf");

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(new CropMargin(ouputfile1.getPath(), 44 * NumUtils.MMtoPT, 20 * NumUtils.MMtoPT, 15 * NumUtils.MMtoPT, 320 * NumUtils.MMtoPT)); // SRA3 : 450x320 mm
        pdfTool.execute(file);

        Assertions.assertTrue(ouputfile1.exists(), "Check if output exists");
        Assertions.assertTrue(ouputfile1.length() > 10, "Check if output is not empty");

        pdfTool = new PdfTool();
        pdfTool.addFilter(new CropMargin(ouputfile2.getPath(), 327 * NumUtils.MMtoPT, 20 * NumUtils.MMtoPT, 15 * NumUtils.MMtoPT, 37 * NumUtils.MMtoPT)); // SRA3 : 450x320 mm
        pdfTool.execute(file);

        Assertions.assertTrue(ouputfile2.exists(), "Check if output exists");
        Assertions.assertTrue(ouputfile2.length() > 10, "Check if output is not empty");
    }

    public void testImposePdf() {

        Impose impose = new Impose(TEST_RESULTS + "plate_{TEMP}.pdf", (20 + 216) * 2 * NumUtils.MMtoPT, (20 + 295) * NumUtils.MMtoPT, Impose.DIVIDE);
        impose.addPageSlot(new PageSlot(10 * NumUtils.MMtoPT, 10 * NumUtils.MMtoPT, true, 110 * NumUtils.MMtoPT, 309 * NumUtils.MMtoPT, "{FILENAME} - Page {FOLIO}", 10));
        impose.addPageSlot(new PageSlot(246 * NumUtils.MMtoPT, 10 * NumUtils.MMtoPT, true, 330 * NumUtils.MMtoPT, 309 * NumUtils.MMtoPT, "Page {FOLIO}", 10));
        impose.addPageMark(new PageMark(3 * NumUtils.MMtoPT, 3 * NumUtils.MMtoPT, 5 * NumUtils.MMtoPT, 5 * NumUtils.MMtoPT, "++||"));
        impose.addPageMark(new PageMark(3 * NumUtils.MMtoPT, 3 * NumUtils.MMtoPT, 5 * NumUtils.MMtoPT, 5 * NumUtils.MMtoPT, "||++"));
        impose.setDeleteSource(true);

        GroupFilter group1 = new GroupFilter();
        group1.addFilter(new InsertPage(TEST_RESULTS + "page_{TEMP}.tmp", 1, 1));
        group1.addFilter(new DuplicatePage(TEST_RESULTS + "plate_{TEMP}.tmp", "2-", 5));
        group1.addFilter(new Multiply(2));
        group1.addFilter(impose);

        GroupFilter group2 = new GroupFilter(3);
        group2.addFilter(new RemovePage(TEST_RESULTS + "page_{TEMP}.tmp", "1")); // SRA3 : 450x320 mm
        group2.addFilter(new Multiply(2));
        group2.addFilter(impose);

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(group1);
        pdfTool.addFilter(group2);

        File file1 = new File(TEST_RESOURCES + "julesverne.pdf");
        pdfTool.execute(file1);

        File file = new File(TEST_RESULTS + "filter.xml");
        pdfTool.saveToFile(file);
        PdfTool.createFromFile(file);
    }

    public void testCropImposeSplit() {
        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(new AddMargin(TEST_RESULTS + "OUT-{BASE}_{COUNT}.{EXT}", 3 * 72 / 25.4f, 3 * 72 / 25.4f, 3 * 72 / 25.4f, 3 * 72 / 25.4f));
        pdfTool.addFilter(new CropMargin(3 * 72 / 25.4f, 3 * 72 / 25.4f, 3 * 72 / 25.4f, 3 * 72 / 25.4f));
        pdfTool.addFilter(new Impose((20 + 216) * 2 * NumUtils.MMtoPT, (20 + 295) * NumUtils.MMtoPT, Impose.DIVIDE, 10 * NumUtils.MMtoPT, 10 * NumUtils.MMtoPT, 246 * NumUtils.MMtoPT, 10 * NumUtils.MMtoPT));
        pdfTool.addFilter(new PaddingPage(4, PaddingPage.LAST_PAGE));
        pdfTool.addFilter(new InsertPage(0, 2));
        pdfTool.addFilter(new Resize(595.4f * NumUtils.MMtoPT, 216 * NumUtils.MMtoPT, 295 * NumUtils.MMtoPT, 216 * NumUtils.MMtoPT));
        pdfTool.addFilter(new RemovePage("1,2"));
        pdfTool.addFilter(new SelectPage("3-"));
        pdfTool.addFilter(new SplitDocument("{BASE}_before.{EXT}", "{BASE}_after.{EXT}", 15));
        pdfTool.addFilter(new SplitPage("OUT-{BASE}_{COUNT}.{EXT}", 3));
        pdfTool.addFilter(new Transform(.5d, .5d, 0));

        File file = new File(TEST_RESOURCES + "julesverne.pdf");
        pdfTool.execute(file);
    }

    public void testConcatPdf() {
        File file = new File(TEST_RESOURCES);
        File[] files = file.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".pdf"));
        Concat concatFilter = new Concat(TEST_RESULTS + file.getName() + "_concatAll_.pdf");

        PdfTool pdfTool = new PdfTool();
        pdfTool.addFilter(concatFilter);
        pdfTool.execute(files);
    }

}
