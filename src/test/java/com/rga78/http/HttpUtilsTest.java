
package com.rga78.http;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HttpUtilsTest {

    @Test
    public void testParseHeaderParameter() {
        
        String headerValue = "attachment;  filename=blah, something-else";
        assertEquals("blah", HttpUtils.parseHeaderParameter(headerValue, "filename") );
        
        headerValue = "application/json;charset=UTF-8";
        assertEquals("UTF-8", HttpUtils.parseHeaderParameter(headerValue, "charset") );
        
        headerValue = "application/json; charset=UTF-8 q=0.8 ";
        assertEquals("UTF-8", HttpUtils.parseHeaderParameter(headerValue, "charset") );
        assertEquals("0.8", HttpUtils.parseHeaderParameter(headerValue, "q") );
    }
}
