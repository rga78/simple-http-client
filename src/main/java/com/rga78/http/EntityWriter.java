package com.rga78.http;

import java.io.OutputStream;

/**
 * EntityWriters are used for marshaling an entity into an HTTP request payload.
 *
 */
public interface EntityWriter {

    /**
     * Write the entity to the given entityStream.
     * 
     * Note the EntityWriter impl is responsible for obtaining a reference to the 
     * entity that will be marshaled.
     * 
     * @param entityStream The stream to which to write the entity.
     */
    public void writeEntity(OutputStream entityStream);

}
