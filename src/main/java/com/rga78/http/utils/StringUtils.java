
package com.rga78.http.utils;

public class StringUtils {

    /**
     * @return true if (s == null || s.trim().isEmpty())
     */
    public static boolean isEmpty( String s) {
        return (s == null || s.trim().isEmpty());
    }

    /**
     * @return the first String that isn't empty, or null if they're all empty
     */
    public static String firstNonEmpty(String... strs) {
        for (String s : strs) {
            if ( !isEmpty(s) ) {
                return s;
            }
        }
        return null;
    }

}

