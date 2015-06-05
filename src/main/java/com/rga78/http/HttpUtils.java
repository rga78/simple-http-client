package com.rga78.http;

import com.rga78.http.utils.StringUtils;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    /**
     * @return a TrustManager configured to trust ALL certificates.
     */
    public static TrustManager getTrustAllCertificates() {
        return new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        };
    }
    
    /**
     * @return a HostnameVerifier configured to trust all hostnames.
     */
    public static HostnameVerifier getTrustAllHostnames() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
    
    /**
     * Set the default SSL context factor to trust all SSL certs and hostnames.
     */
    public static void setDefaultTrustAllCertificates() {

        try {
            SSLContext sc =  SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { getTrustAllCertificates() } , new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(getTrustAllHostnames());
            
            // Set as default for SimpleHttpClient.getConnection
            SSLContext.setDefault(sc);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
