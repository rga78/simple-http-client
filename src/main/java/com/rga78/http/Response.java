package com.rga78.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;

import com.rga78.http.utils.ObjectUtils;
import com.rga78.http.utils.IOUtils;

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
            
    /**
     * Write the response to the given OutputStream.
     * 
     * If the response is plain text, be mindful of char encoding.
     * If no char-encoding is specified, default to UTF-8.
     * 
     * @param outputStream write the response to this stream
     */
    public void copyToStream( OutputStream outputStream) throws IOException {
        
        String contentType = getHeader("Content-Type");
        
        if ( contentType.contains("text/plain") ) {
            // Plain text response.  Handle the char encoding.
            String charsetName = ObjectUtils.firstNonNull( HttpUtils.parseHeaderParameter(contentType, "charset"), "UTF-8");
            
            // The OutputStreamWriter deliberately uses the default platform encoding because,
            // well, what else should we use?
            IOUtils.copyReader( new InputStreamReader( getInputStream(), charsetName),
                                new OutputStreamWriter(outputStream) );
        } else {
            // Binary response.  Copy as-is.
            IOUtils.copyStream( getInputStream(), outputStream );
        }
    }
            
}
