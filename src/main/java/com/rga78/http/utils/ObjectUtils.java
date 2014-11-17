
package com.rga78.http.utils;

/**
 * A lite-weight impl of apache.commons.lang3.ObjectUtils.
 */
public class ObjectUtils {

    /**
     * @return the first param that isn't null, or null if they're all null.
     */
    public static <T> T firstNonNull(T... vals) {
        for (T val : vals) {
            if (val != null) {
                return val;
            }
        }
        return null;
    }

}
