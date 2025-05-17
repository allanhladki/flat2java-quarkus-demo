package com.github.allanhladki.flat2java.quarkus.demo.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.github.allanhladki.flat2java.markers.FlatField;

import lombok.Data;

@Data
public class SampleModel {

    private final UUID uuid = UUID.randomUUID();
    
    @FlatField(position = 1, length = 30)
    private String name;
    
    @FlatField(position = 2, length = 10)
    private Integer quantity;
    
    @FlatField(position = 3, length = 20)
    private Long transactionId;
    
    @FlatField(position = 4, length = 15, precision = 2)
    private BigDecimal amount;
    
    @FlatField(position = 5, length = 20)
    private BigInteger accountNumber;
    
    @FlatField(position = 6, length = 8, dateTimeFormat = "yyyyMMdd")
    private LocalDate birthDate;
    
    @FlatField(position = 7, length = 26, dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

} 