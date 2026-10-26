package io.github.allanhladki.flat2java.quarkus.demo.model;

import io.github.allanhladki.flat2java.annotation.FlatField;
import io.quarkus.runtime.annotations.RegisterForReflection;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

/** An element of the {@code items} list: 6 + 3 + 9 = 18 columns. */
@RegisterForReflection
public record Item(
        @Schema(description = "Product code (6 columns).", example = "AB-100")
                @FlatField(length = 6)
                String sku,
        @Schema(description = "Units (3 columns).", example = "2") @FlatField(length = 3) int units,
        @Schema(
                        description = "Unit price with two implied decimal places (9 columns).",
                        example = "12.50")
                @FlatField(length = 9, precision = 2)
                BigDecimal unitPrice) {}
