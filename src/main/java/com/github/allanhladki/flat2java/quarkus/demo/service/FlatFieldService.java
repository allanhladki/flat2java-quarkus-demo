package com.github.allanhladki.flat2java.quarkus.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.allanhladki.flat2java.core.FieldMetadata;
import com.github.allanhladki.flat2java.core.FieldMetadataExtractor;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FlatFieldService {

    private static final Logger logger = LoggerFactory.getLogger(FlatFieldService.class);

    public void toPojo(String plainText, Class<?> clazz){

        List<FieldMetadata> metadata = FieldMetadataExtractor.extract(clazz);

        metadata.forEach(field -> logger.debug("Field: {}", field));

    }

    public <T> void fromPojo(T pojo){

        List<FieldMetadata> metadata = FieldMetadataExtractor.extract(pojo.getClass());

        metadata.forEach(field -> logger.debug("Field: {}", field));

    }


}
