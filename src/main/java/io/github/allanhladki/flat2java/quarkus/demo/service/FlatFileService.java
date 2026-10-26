package io.github.allanhladki.flat2java.quarkus.demo.service;

import io.github.allanhladki.flat2java.Flat2Java;
import io.github.allanhladki.flat2java.quarkus.demo.model.SampleModel;
import io.quarkus.logging.Log;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * Reads and writes {@link SampleModel} records with flat2java. Only lengths are logged: a record
 * usually holds personal data.
 */
@ApplicationScoped
public class FlatFileService {

    private final Flat2Java flat2Java;

    @Inject
    FlatFileService(Flat2Java flat2Java) {
        this.flat2Java = flat2Java;
    }

    /**
     * Reads a record.
     *
     * @param line the record, without line terminator
     * @return the object
     */
    public SampleModel read(String line) {
        Log.debugf("Reading a record of %d characters", line.length());
        return flat2Java.read(line, SampleModel.class);
    }

    /**
     * Writes a record.
     *
     * @param model the object
     * @return the record, without line terminator
     * @throws BadRequestException if the object is missing
     */
    public String write(SampleModel model) {
        if (model == null) {
            throw new BadRequestException("The request body is required");
        }
        String line = flat2Java.write(model);
        Log.debugf("Wrote a record of %d characters", line.length());
        return line;
    }

    /**
     * Reads the record in a UTF-8 file. One trailing line terminator is ignored.
     *
     * @param location the path of the file
     * @return the object
     * @throws BadRequestException if the path is blank or invalid, or the file is too large to hold
     *     one record or is not UTF-8 text
     * @throws NotFoundException if there is no file at the path
     */
    public SampleModel readFile(String location) {
        if (location == null || location.isBlank()) {
            throw new BadRequestException("The path parameter is required");
        }
        Path path;
        try {
            path = Path.of(location);
        } catch (InvalidPathException e) {
            throw new BadRequestException("Invalid path: " + location);
        }
        if (!Files.isRegularFile(path)) {
            throw new NotFoundException("File not found: " + path);
        }
        String content;
        try {
            // A record has at most 4 bytes per character, plus a line terminator. Checking the
            // size first keeps a huge file out of memory.
            long limit = 4L * flat2Java.recordLength(SampleModel.class) + 2;
            if (Files.size(path) > limit) {
                throw new BadRequestException(
                        "The file is larger than one record (at most "
                                + limit
                                + " bytes): "
                                + path);
            }
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            throw new NotFoundException("File not found: " + path);
        } catch (CharacterCodingException e) {
            throw new BadRequestException("The file is not UTF-8 text: " + path);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + path, e);
        }
        Log.debugf("Read %d characters from %s", content.length(), path);
        return read(withoutLineTerminator(content));
    }

    private static String withoutLineTerminator(String content) {
        if (content.endsWith("\r\n")) {
            return content.substring(0, content.length() - 2);
        }
        if (content.endsWith("\n")) {
            return content.substring(0, content.length() - 1);
        }
        return content;
    }
}
