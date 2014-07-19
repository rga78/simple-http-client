package com.rga78.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.rga78.http.junit.CaptureSystemOutRule;
import com.rga78.http.log.Log;

/**
 * Unit tests..
 */
public class SimpleHttpClientTest {

    /**
     * Capture and suppress stdout unless the test fails.
     */
    @Rule
    public CaptureSystemOutRule systemOutRule  = new CaptureSystemOutRule( );
    
    /**
     * 
     */
    @Test
    public void basicTest() throws Exception {
        
        List<String> response = new SimpleHttpClient()
                                        .setTarget("http://www.google.com/")
                                        .header("Accept", "text/html")
                                        .get()
                                        .readEntity( new StringEntityReader() );
        
        Log.trace(this, "basicTest: ", response);
        
        assertFalse( response.isEmpty() );
        assertTrue( response.get(0).startsWith( "<!doctype html>" ) );
    }
    
    /**
     * 
     */
    @Test
    public void testPath() {
        
        SimpleHttpClient shc = new SimpleHttpClient()
                                        .path("hello")
                                        .path("my")
                                        .path("name/is")
                                        .path("rob");
        
        assertEquals( "/hello/my/name/is/rob", shc.getPath() );
        
        assertEquals( "//hello", new SimpleHttpClient().path("/hello").getPath() );
        assertEquals( "/hello//there", new SimpleHttpClient().path("hello").path("").path("there").getPath() );
    }
    
    /**
     * 
     */
    @Test
    public void testQueryParam() {
        
        String queryString = new SimpleHttpClient().queryParam("foo", "bar").queryParam("hello", "world").getQueryString();
        
        assertTrue(queryString.equals("?foo=bar&hello=world")
                   || queryString.equals("?hello=world&foo=bar") );
                    
    }
}
