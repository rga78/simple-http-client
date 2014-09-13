package com.rga78.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

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
     * @return HttpURLConnection.getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        try {
            return con.getInputStream();
        } catch (IOException e) {
            handleFailureResponse(e);
            throw e;
        }
    }
    
    /**
     * Read any error response from the connection and include it in the thrown exception.
     * 
     * @throws IOException
     */
    protected void handleFailureResponse(IOException e) throws IOException {
        List<String> errorResponse = new StringEntityReader().readEntity( con.getErrorStream() );
        if (errorResponse.isEmpty()) {
            throw e;    // No additional error info.
        } else {
            throw new IOException( e.getMessage() + ": " + errorResponse.toString(), e);
        }
    }

    /**
     * @return HttpURLConnection.getHeaderField()
     */
    public String getHeader(String name) {
        return con.getHeaderField(name);
    }
            
}
