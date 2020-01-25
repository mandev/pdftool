package com.adlitteram.pdftool.xml;

import com.adlitteram.pdftool.utils.NumUtils;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import java.awt.Color;

public class ColorConverter implements SingleValueConverter {

    @Override
    public String toString(Object obj) {
        Color color = (Color) obj;
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    @Override
    public Object fromString(String name) {
        String[] cc = name.split(",");
        return new Color(NumUtils.intValue(cc[0]), NumUtils.intValue(cc[1]), NumUtils.intValue(cc[2]));
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(Color.class);
    }
}
