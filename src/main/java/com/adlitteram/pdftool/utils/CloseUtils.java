package com.adlitteram.pdftool.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;

public class CloseUtils {

    public static void closeQuietly(Document doc) {
        try {
            if (doc != null) {
                doc.close();
            }
        }
        catch (Exception e) {
        }
    }

    public static void closeQuietly(PdfReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        }
        catch (Exception e) {
        }
    }

}
