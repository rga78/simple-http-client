package com.rga78.http;

import com.rga78.http.utils.StringUtils;

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
        
        if (StringUtils.isEmpty(headerValue)) {
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
     * @return a base64-encoded "Authorization" header value.
     */
    public static String buildBasicAuthHeaderValue(String user, String pass) {
        return buildBasicAuthHeaderValue(user + ":" + pass);
    }

    /**
     * @return a base64-encoded "Authorization" header value.
     */
    public static String buildBasicAuthHeaderValue(String userAndPass) {
        return "Basic " + Base64Coder.base64Encode(userAndPass);
    }


}
