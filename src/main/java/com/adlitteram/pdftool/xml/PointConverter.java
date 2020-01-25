package com.adlitteram.pdftool.xml;

import com.adlitteram.pdftool.utils.NumUtils;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class PointConverter implements SingleValueConverter {

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public String toString(Object obj) {
        Float number = (Float) obj;
        return NumUtils.toUnit(number, NumUtils.MM, 2);
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Object fromString(String name) {
        return NumUtils.pointValue(name);
    }

    /**
     *
     * @param type
     * @return
     */
    @Override
    public boolean canConvert(Class type) {
        return type.equals(float.class);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();
        toStringBuilder.append(super.toString());
        toStringBuilder.append("\n");
        return toStringBuilder.toString();
    }
}
