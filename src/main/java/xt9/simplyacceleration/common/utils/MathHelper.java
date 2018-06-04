package xt9.simplyacceleration.common.utils;

/**
 * Created by xt9 on 2018-05-28.
 */
public class MathHelper {
    public static int ensureRange(int value, int min, int max) {
        return java.lang.Math.min(java.lang.Math.max(value, min), max);
    }

    public static int getIntFromStringSafe(String str) {
        int result = 0;
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException exc) {
            return result;
        }
    }

    public static boolean isStringNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch(NumberFormatException exc) {
            return false;
        }
        return true;
    }
}
