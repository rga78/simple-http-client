package com.rga78.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and returns the Entity as a List<String>, one string per line.
 */
public class StringEntityReader implements EntityReader<List<String>> {

    /**
     * @param entityStream Note: entityStream is closed at end of method.
     * 
     * @return List<String> - the contents of the entityStream, one entry per line.
     */
    @Override
    public List<String> readEntity(InputStream entityStream) throws IOException {

        List<String> retMe = new ArrayList<String>();
        
        if (entityStream == null) {
            return retMe;
        }
        
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(entityStream, Charset.forName("UTF-8")));
        while ( (line = br.readLine()) != null ) {
            retMe.add(line);
        }
        
        entityStream.close();
        return retMe;
    }

}
