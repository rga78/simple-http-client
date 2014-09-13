package com.rga78.http;


public class HttpUtils {
    
    /**
     * 
     * Header parameters are in the form {parmName}={parmValue}.
     * 
     * This method assumes there are no spaces, commas, or semi-colons in the
     * parmName or parmValue.
     *
     * @return ("text/plain; charset=UTF-8", "charset") ==> "UTF-8"
     * 
     */
    public static String parseHeaderParameter(String headerValue, String parmName) {
        
        if (isEmpty(headerValue)) {
            return null;
        }
        
        for (String segment : headerValue.split(";|\\s+|,")) {
            if (segment.startsWith(parmName + "=")) {
                return segment.substring( (parmName + "=").length() ) ;
            }
        }
        
        return null;
    }

    /**
     * @return true if (s == null || s.trim().isEmpty())
     */
    protected static boolean isEmpty( String s) {
        return (s == null || s.trim().isEmpty());
    }

}
