package com.github.allanhladki.flat2java.quarkus.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.allanhladki.flat2java.exception.DeserializationException;
import com.github.allanhladki.flat2java.exception.SerializationException;
import com.github.allanhladki.flat2java.exception.ValidationException;
import com.github.allanhladki.flat2java.interfaces.ObjectDeserializationInterface;
import com.github.allanhladki.flat2java.interfaces.ObjectSerializationInterface;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FlatFieldService {

    private static final Logger logger = LoggerFactory.getLogger(FlatFieldService.class);

    @Inject
    private ObjectDeserializationInterface objectDeserializer;

    @Inject
    private ObjectSerializationInterface objectSerializer;

    /**
     * Converts flat text to POJO with validation
     * @param flatText the flat text to convert
     * @param clazz the target class
     * @return the converted POJO
     * @throws ValidationException if validation fails
     * @throws DeserializationException if deserialization fails
     */
    public <T> T toPojo(String flatText, Class<T> clazz) {
        logger.info("Converting flat text to POJO for class: {}", clazz.getSimpleName());
        
        T result = objectDeserializer.deserialize(flatText, clazz);
        
        if (result == null) {
            throw new DeserializationException("Failed to deserialize flat text to POJO - result is null");
        }
        
        logger.info("Successfully converted flat text to POJO");
        return result;
    }

    /**
     * Converts POJO to flat text with validation
     * @param pojo the POJO to convert
     * @return the flat text representation
     * @throws ValidationException if validation fails
     * @throws SerializationException if serialization fails
     */
    public <T> String fromPojo(T pojo) {
        logger.info("Converting POJO to flat text: {}", pojo != null ? pojo.getClass().getSimpleName() : "null");
        
        
        String result = objectSerializer.serialize(pojo);
        
        if (result == null || result.isEmpty()) {
            throw new SerializationException("Failed to serialize POJO to flat text - result is null or empty");
        }
        
        logger.info("Successfully converted POJO to flat text, length: {}", result.length());
        return result;
    }

}
