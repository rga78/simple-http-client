package com.rga78.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * An HTTP Response object.  It contains methods for reading the HTTP response.
 */
public class Response {

    /**
     * The connection from which to read the response.
     */
    private HttpURLConnection con;
    
    /**
     * CTOR.
     */
    protected Response(HttpURLConnection con) {
        this.con = con;
    }

    /**
     * Use the provided EntityReader to read and parse the response.
     * 
     * @param entityReader The response stream is passed to this entityReader for parsing
     * 
     * @return the entity as read by the given entityReader.
     */
    public <T> T readEntity(EntityReader<T> entityReader) throws IOException {
        return entityReader.readEntity( getInputStream() );
    }
    
    /**
     * 
     */
    protected InputStream getInputStream() throws IOException {
        try {
            return con.getInputStream();
        } catch (IOException e) {
            handleFailureResponse(e);
            throw e;
        }
    }
    
    /**
     * 
     */
    protected void handleFailureResponse(IOException e) throws IOException {
        throw new IOException( e.getMessage() + ": " + new StringEntityReader().readEntity( con.getErrorStream() ).toString(), e);
    }
            
}
