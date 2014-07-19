package com.rga78.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * EntityReaders are used for parsing HTTP response data into an entity object.
 * 
 * @param T The entity type that is returned by readEntity.
 * 
 */
public interface EntityReader<T> {

    /**
     * @param entityStream Raw data stream (typically an HTTP response) containing the 
     *                     entity to be parsed
     * 
     * @return the entity read from the given entityStream.
     */
    public T readEntity(InputStream entityStream) throws IOException;

}
