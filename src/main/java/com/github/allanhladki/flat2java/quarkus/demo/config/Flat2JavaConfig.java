package com.github.allanhladki.flat2java.quarkus.demo.config;

import com.github.allanhladki.flat2java.interfaces.FieldDeserializationInterface;
import com.github.allanhladki.flat2java.interfaces.FieldMetadataInterface;
import com.github.allanhladki.flat2java.interfaces.FieldSerializationInterface;
import com.github.allanhladki.flat2java.interfaces.ObjectDeserializationInterface;
import com.github.allanhladki.flat2java.interfaces.ObjectSerializationInterface;
import com.github.allanhladki.flat2java.impl.FieldDeserializationImpl;
import com.github.allanhladki.flat2java.impl.FieldMetadataImpl;
import com.github.allanhladki.flat2java.impl.FieldSerializationImpl;
import com.github.allanhladki.flat2java.impl.ObjectDeserializationImpl;
import com.github.allanhladki.flat2java.impl.ObjectSerializationImpl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * Configuration class that provides CDI producers for flat2java implementations.
 * This ensures proper dependency injection and allows the library to work seamlessly
 * with Quarkus CDI container.
 */
@ApplicationScoped
public class Flat2JavaConfig {

    /**
     * Produces ObjectDeserializationInterface implementation for CDI injection.
     */
    @Produces
    @ApplicationScoped
    public ObjectDeserializationInterface objectDeserializer() {
        return new ObjectDeserializationImpl();
    }

    /**
     * Produces ObjectSerializationInterface implementation for CDI injection.
     */
    @Produces
    @ApplicationScoped
    public ObjectSerializationInterface objectSerializer() {
        return new ObjectSerializationImpl();
    }

    /**
     * Produces FieldDeserializationInterface implementation for CDI injection.
     */
    @Produces
    @ApplicationScoped
    public FieldDeserializationInterface fieldDeserializer() {
        return new FieldDeserializationImpl();
    }

    /**
     * Produces FieldSerializationInterface implementation for CDI injection.
     */
    @Produces
    @ApplicationScoped
    public FieldSerializationInterface fieldSerializer() {
        return new FieldSerializationImpl();
    }

    /**
     * Produces FieldMetadataInterface implementation for CDI injection.
     */
    @Produces
    @ApplicationScoped
    public FieldMetadataInterface fieldMetadata() {
        return new FieldMetadataImpl();
    }
}
