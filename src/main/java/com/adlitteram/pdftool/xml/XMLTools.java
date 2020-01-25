package com.adlitteram.pdftool.xml;

import com.adlitteram.pdftool.filters.AddMetadata;
import com.adlitteram.pdftool.filters.PdfTool;
import com.adlitteram.pdftool.filters.CropMark;
import com.adlitteram.pdftool.filters.GroupFilter;
import com.adlitteram.pdftool.filters.Impose;
import com.adlitteram.pdftool.filters.PageMark;
import com.adlitteram.pdftool.filters.PageSlot;
import com.adlitteram.pdftool.filters.RenderFilter;
import com.thoughtworks.xstream.XStream;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLTools {

    public static final XStream XSTREAM = new XStream();

    static {
        XSTREAM.aliasPackage("", "com.adlitteram.pdftool.filters");

        XSTREAM.useAttributeFor(Color.class);
        XSTREAM.useAttributeFor(String.class);
        XSTREAM.useAttributeFor(double.class);
        XSTREAM.useAttributeFor(float.class);
        XSTREAM.useAttributeFor(int.class);
        XSTREAM.useAttributeFor(boolean.class);

        XSTREAM.registerConverter(new PointConverter());
        XSTREAM.registerConverter(new ColorConverter());

        XSTREAM.addImplicitCollection(AddMetadata.class, "metadatas");
        XSTREAM.addImplicitCollection(CropMark.class, "marks");
        XSTREAM.addImplicitCollection(Impose.class, "pageSlots", PageSlot.class);
        XSTREAM.addImplicitCollection(Impose.class, "pageMarks", PageMark.class);
        XSTREAM.addImplicitCollection(GroupFilter.class, "filters");
        XSTREAM.addImplicitCollection(PdfTool.class, "filters");
        XSTREAM.addImplicitCollection(RenderFilter.class, "renders");
    }

    private XMLTools() {
    }

    /**
     *
     * @param object
     * @param file
     * @throws IOException
     */
    public static void encodeToFile(Object object, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"), 512)) {
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            XSTREAM.toXML(object, writer);
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Object decodeFromFile(File file) throws FileNotFoundException, IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return XSTREAM.fromXML(fis);
        }
    }
}
