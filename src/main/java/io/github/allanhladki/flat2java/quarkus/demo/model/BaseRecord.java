package io.github.allanhladki.flat2java.quarkus.demo.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import io.github.allanhladki.flat2java.annotation.FlatField;
import io.quarkus.runtime.annotations.RegisterForReflection;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * Fields shared by every record of the file. flat2java writes the fields of a superclass before the
 * fields of its subclasses.
 */
@RegisterForReflection
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class BaseRecord {

    @Schema(description = "Record type code (4 columns).", example = "SAMP")
    @FlatField(length = 4)
    private String recordType;

    @Schema(description = "Layout version (2 columns).", example = "1")
    @FlatField(length = 2)
    private int schemaVersion;
}
