package com.github.allanhladki.flat2java.quarkus.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public <T> T toPojo(String plainText, Class<T> clazz) {
        try {
            logger.info("Converting flat text to POJO for class: {}", clazz.getSimpleName());
            T result = objectDeserializer.deserialize(plainText, clazz);
            logger.info("Successfully converted flat text to POJO: {}", result != null ? "success" : "null result");
            return result;
        } catch (Exception e) {
            logger.error("Error converting flat text to POJO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert flat text to POJO", e);
        }
    }

    public <T> String fromPojo(T pojo) {
        try {
            logger.info("Converting POJO to flat text: {}", pojo != null ? pojo.getClass().getSimpleName() : "null");
            String result = objectSerializer.serialize(pojo);
            logger.info("Successfully converted POJO to flat text, length: {}", result != null ? result.length() : 0);
            return result;
        } catch (Exception e) {
            logger.error("Error converting POJO to flat text: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert POJO to flat text", e);
        }
    }
}
