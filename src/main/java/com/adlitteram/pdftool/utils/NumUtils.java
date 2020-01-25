package com.adlitteram.pdftool.utils;

import java.math.BigDecimal;
import java.util.StringTokenizer;

public class NumUtils {

    public static final float INtoPT = 72;
    public static final float PTtoIN = 1f / 72f;
    public static final float MMtoPT = 72f / 25.4f;
    public static final float PTtoMM = 25.4f / 72f;
    public static final float CMtoPT = 72f / 2.54f;
    public static final float PTtoCM = 2.54f / 72f;
    public static final int MM = 0;
    public static final int CM = 1;
    public static final int PT = 2;
    public static final int IN = 3;

    /**
     * Convert String to Point value
     *
     * @param str
     * @return
     * @throws NumberFormatException
     */
    public static float pointValue(String str) throws NumberFormatException {
        str = str.trim();
        int len = str.length() - 2;
        if (len > 0) {
            String unit = str.substring(len).toLowerCase();
            if (unit.compareTo("pt") == 0) {
                return Float.parseFloat(str.substring(0, len));
            }
            if (unit.compareTo("mm") == 0) {
                return MMtoPT * Float.parseFloat(str.substring(0, len));
            }
            if (unit.compareTo("cm") == 0) {
                return CMtoPT * Float.parseFloat(str.substring(0, len));
            }
            if (unit.compareTo("in") == 0) {
                return INtoPT * Float.parseFloat(str.substring(0, len));
            }
        }
        return Float.parseFloat(str);
    }

    /**
     *
     * @param value
     * @param unit
     * @param accuracy
     * @return
     */
    public static String toUnit(float value, int unit, int accuracy) {
        switch (unit) {
            case MM:
                return roundDecimal(value * PTtoMM, accuracy) + " mm";

            case IN:
                return roundDecimal(value * PTtoIN, accuracy) + " in";

            case CM:
                return roundDecimal(value * PTtoCM, accuracy) + " cm";
        }
        return String.valueOf(value);
    }

    /**
     *
     * @param d
     * @param accuracy
     * @return
     */
    public static String roundDecimal(float d, int accuracy) {
        BigDecimal bd = new BigDecimal(d);
        return String.valueOf(bd.setScale(accuracy, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean booleanValue(Object obj) {
        return (obj instanceof String) ? Boolean.parseBoolean((String) obj) : ((Boolean) obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static int intValue(Object obj) {
        return (obj instanceof String) ? Integer.parseInt((String) obj) : ((Integer) obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static long longValue(Object obj) {
        return (obj instanceof String) ? Long.parseLong((String) obj) : ((Long) obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static float floatValue(Object obj) {
        return (obj instanceof String) ? Float.parseFloat((String) obj) : ((Float) obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public static double doubleValue(Object obj) {
        return (obj instanceof String) ? Double.parseDouble((String) obj) : ((Double) obj);
    }

    /**
     *
     * @param obj
     * @param value
     * @return
     */
    public static boolean booleanValue(Object obj, boolean value) {
        try {
            return booleanValue(obj);
        }
        catch (RuntimeException e) {
            return value;
        }
    }

    /**
     *
     * @param obj
     * @param value
     * @return
     */
    public static int intValue(Object obj, int value) {
        try {
            return intValue(obj);
        }
        catch (RuntimeException e) {
            return value;
        }
    }

    /**
     *
     * @param obj
     * @param value
     * @return
     */
    public static long longValue(Object obj, long value) {
        try {
            return longValue(obj);
        }
        catch (RuntimeException e) {
            return value;
        }
    }

    /**
     *
     * @param obj
     * @param value
     * @return
     */
    public static float floatValue(Object obj, float value) {
        try {
            return floatValue(obj);
        }
        catch (RuntimeException e) {
            return value;
        }
    }

    /**
     *
     * @param obj
     * @param value
     * @return
     */
    public static double doubleValue(Object obj, double value) {
        try {
            return doubleValue(obj);
        }
        catch (RuntimeException e) {
            return value;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean isValidBoolean(Object obj) {
        try {
            booleanValue(obj);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean isValidInt(Object obj) {
        try {
            intValue(obj);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean isValidLong(Object obj) {
        try {
            longValue(obj);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean isValidFloat(Object obj) {
        try {
            floatValue(obj);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static boolean isValidDouble(Object obj) {
        try {
            doubleValue(obj);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }

    // Math
    /**
     *
     * @param a
     * @return
     */
    public static boolean isEven(int a) {    // pair
        return (a & 1) == 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isEven(long a) {
        return (a & 1L) == 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isEven(double a) {
        return a % 2 == 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isEven(float a) {
        return a % 2 == 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isOdd(int a) {    // impair
        return (a & 1) != 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isOdd(long a) {
        return (a & 1L) != 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isOdd(double a) {
        return a % 2 != 0;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean isOdd(float a) {
        return a % 2 != 0;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toByteSize(long s) {
        if (s < 1024) {
            return s + " B";
        }
        else if (s < 1024 * 1024 * 1024) {
            return Math.round(s / 1024) + " Ko";
        }
        else if (s < 1024 * 1024 * 1024 * 1024) {
            return Math.round(s / (1024 * 1024)) + " Mo";
        }
        else {
            return Math.round(s / (1024 * 1024 * 1024)) + " Go";
        }
    }

    /**
     *
     * @param min
     * @param value
     * @param max
     * @return
     */
    public static int clamp(int min, int value, int max) {
        return (value < min) ? min : (value > max) ? max : value;
    }

    // String to value
    /**
     *
     * @param str
     * @return
     */
    public static int[] stringToIntArray(String str) {
        StringTokenizer st = new StringTokenizer(str);
        int[] array = new int[st.countTokens()];
        for (int i = 0; i < array.length; i++) {
            array[i] = intValue(st.nextToken());
        }
        return array;
    }

    /**
     *
     * @param array
     * @return
     */
    public static String intArrayToString(int[] array) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i]).append(" ");
        }
        return buffer.toString();
    }

    /**
     *
     * @param str
     * @return
     */
    public static float[] stringToFloatArray(String str) {
        StringTokenizer st = new StringTokenizer(str);
        float[] array = new float[st.countTokens()];
        for (int i = 0; i < array.length; i++) {
            array[i] = floatValue(st.nextToken());
        }
        return array;
    }

    /**
     *
     * @param array
     * @return
     */
    public static String floatArrayToString(float[] array) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i]).append(" ");
        }
        return buffer.toString();
    }

    /**
     *
     * @param str
     * @return
     */
    public static double[] stringToDoubleArray(String str) {
        StringTokenizer st = new StringTokenizer(str);
        double[] array = new double[st.countTokens()];
        for (int i = 0; i < array.length; i++) {
            array[i] = doubleValue(st.nextToken());
        }
        return array;
    }

    /**
     *
     * @param array
     * @return
     */
    public static String doubleArrayToString(double[] array) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i]).append(" ");
        }
        return buffer.toString();
    }
}
