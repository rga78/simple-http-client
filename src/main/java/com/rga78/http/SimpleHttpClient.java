package com.rga78.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Very simple http client featuring a fluent API.
 * 
 * Example usage: send a "GET" request to "http://myhost:8080/my/uri/path" and return
 * the response as a List<String> (one per line):
 * 
 * <pre><code>
 *  List<String> response = new SimpleHttpClient()
 *                                   .setTarget( "http://myhost:8080" )
 *                                   .path("my/uri")
 *                                   .path("path")
 *                                   .header( "Accept", "text/plain" )
 *                                   .get()
 *                                   .readEntity( new StringEntityReader() );
 * </code></pre>
 */
public class SimpleHttpClient {

    /**
     * The URL path.
     */
    private String path = "";
    
    /**
     * The URL target ( {protocol}://{host}:{port} )
     */
    private String target;

    /**
     * Map of HTTP headers.
     */
    private Map<String, String> headers = new HashMap<String, String>();

    /**
     * Map of query params.
     * 
     * TODO: the order in which query params are written out is currently undefined
     *       given that i'm using a Map.  Should probably write them in the order
     *       they are provided (not that order matters I don't think, unless a parm
     *       has more than 1 value, in which case the order of the list of values
     *       for that parm may or may not be important to the server).
     */
    private Map<String, String> queryParams = new HashMap<String, String>();


    /**
     * Set the target.
     * 
     * @param target {protocol}://{host}:{port}
     * 
     * @return this
     */
    public SimpleHttpClient setTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * @return the target ( {protocol}://{host}:{port} )
     */
    protected String getTarget() {
        return target;
    }

    /**
     * Append the given appendPath to the path.
     *
     * Note: a "/" is automatically inserted in front of appendPath.
     * 
     * @param appendPath The path segment(s) to append, e.g. "foo", "foo/bar"
     * 
     * @return this
     */
    public SimpleHttpClient path(String appendPath) {
        path += "/" + appendPath;
        return this;
    }

    /**
     * @return the constructed path
     */
    protected String getPath() {
        return path;
    }

    /**
     * @return the query string (including the leading "?"), or "" if none.
     */
    protected String getQueryString() {
        
        StringBuilder retMe = new StringBuilder();
        String delim = "";

        for (Map.Entry<String, String> queryParam : getQueryParams().entrySet()) {
            retMe.append(delim).append(queryParam.getKey() + "=" + queryParam.getValue());
            delim = "&";
        }

        return (retMe.length() == 0) ? "" : "?" + retMe.toString();
    }

    /**
     * @return the target URL
     */
    protected URL getURL() throws IOException {
        return new URL( getTarget() + getPath() + getQueryString() );  
    }

    /**
     * Set the given header.
     * 
     * @param key header key (e.g "Accept")
     * @param value header value (e.g "text/plain")
     *
     * @return this.
     */
    public SimpleHttpClient header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * @return the header map
     */
    protected Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * TODO: handle multiple values.
     * 
     * @param key parm key
     * @param value parm value
     * 
     * @return this
     */
    public SimpleHttpClient queryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    /**
     * @return the queryParams map.
     */
    protected Map<String, String> getQueryParams() {
        return queryParams;
    }

    // TODO: pathParam
    // TODO: trust all certs
    // TODO: hostname verifier? (not sure this is needed if you already have trust all certs)
    // TODO: close the httpurlcon, inputstreams and whatnot?
    

    /**
     * Execute a GET request.
     *
     * @return a Response object for reading the response
     */
    public Response get() throws IOException {
        HttpURLConnection con = setHeaders( getConnection("GET") );
        con.connect();
        return new Response(con);
    }

    /**
     * Execute a POST request.
     * 
     * @param entityWriter The request's OutputStream is passed to this EntityWriter for marshaling
     *                     the request payload.
     *
     * @return a Response object for reading the response
     * 
     * @throws IOException 
     */
    public Response post( EntityWriter entityWriter ) throws IOException {
        
        return withPayload("POST", entityWriter);
    }
    
    /**
     * Execute a PUT request.
     * 
     * @param entityWriter The request's OutputStream is passed to this EntityWriter for marshaling
     *                     the request payload.
     *
     * @return a Response object for reading the response
     * 
     * @throws IOException 
     */
    public Response put( EntityWriter entityWriter ) throws IOException {
        
        return withPayload("PUT", entityWriter);
    }
    
    /**
     * Both PUTs and POSTs route to here.
     * 
     * @return a Response object for reading the response
     * 
     * @throws IOException 
     */
    protected Response withPayload(String requestMethod, EntityWriter entityWriter) throws IOException {
    
        HttpURLConnection con = setHeaders( getConnection(requestMethod) );
        if (entityWriter != null) {
            entityWriter.writeEntity( con.getOutputStream() );
        }
        con.connect();
        return new Response(con);
    }

    /**
     * @return the given con, with all headers set.
     */
    protected HttpURLConnection setHeaders(HttpURLConnection con) {

        for (Map.Entry<String, String> header : getHeaders().entrySet()) {
            con.setRequestProperty( header.getKey(), header.getValue() );
        }

        return con;
    }

    /**
     * @return an HttpUrlConnection using the given requestMethod (e.g. "GET", "POST", etc).
     */
    protected HttpURLConnection getConnection(String requestMethod ) throws IOException {
        HttpURLConnection con = (HttpURLConnection) getURL().openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);  // should i bother setting this to false for GETs?
        con.setUseCaches(false);
        con.setRequestMethod( requestMethod );
        // TODO: con.setConnectTimeout(timeout);
        return con;
    }

}
