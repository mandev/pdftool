package com.adlitteram.pdftool.filters;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rename extends AbstractPdfFilter {

    protected String newname;
    protected String pattern;
    protected Pattern compiled;

    /**
     * Change the output name of the document (the file is not moved on the file system)
     *
     * @param pattern
     * @param newname
     */
    public Rename(String pattern, String newname) {
        this.pattern = pattern;
        this.newname = newname;
    }

    /**
     *
     * @param inputFile
     * @return
     */
    @Override
    public File[] filter(File inputFile) {
        Set outputFiles = new LinkedHashSet();

        try {
            String outputName = rename(inputFile.getPath());
            outputFiles.add(new File(outputName));

            if (deleteSource) inputFile.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        File[] array = new File[outputFiles.size()];
        outputFiles.toArray(array);
        return array;
    }

    /**
     *
     * @param src
     * @return
     */
    public String rename(String src) {

        if (pattern != null && pattern.length() > 0) {
            try {
                if (compiled == null) compiled = Pattern.compile(this.pattern);

                Matcher m = compiled.matcher(src);

                if (m.matches()) {
                    int state = 0;
                    //int count = m.groupCount();
                    StringBuilder trg = new StringBuilder();
                    StringBuffer value = null;

                    for (int i = 0; i < newname.length(); i++) {
                        char c = newname.charAt(i);

                        if (state == 0) {
                            if (c == '{') {
                                value = new StringBuffer(2);
                                state = 1;
                            }
                            else
                                trg.append(c);
                        }
                        else {
                            if (c == '}') {
                                int j = Integer.parseInt(value.toString());
                                trg.append(m.group(j));
                                state = 0;
                            }
                            else
                                value.append(c);
                        }
                    }

                    return trg.toString();
                }
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
        return src;
    }
}
