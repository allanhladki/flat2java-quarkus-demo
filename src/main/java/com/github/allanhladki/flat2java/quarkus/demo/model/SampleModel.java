package com.github.allanhladki.flat2java.quarkus.demo.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.github.allanhladki.flat2java.markers.FlatField;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SampleModel {

    private final UUID uuid = UUID.randomUUID();
    
    @FlatField(length = 30)
    private String name;
    
    @FlatField(length = 10)
    private Integer quantity;
    
    @FlatField(length = 20)
    private Long transactionId;
    
    @FlatField(length = 15, precision = 2)
    private BigDecimal amount;
    
    @FlatField(length = 20)
    private BigInteger accountNumber;
    
    @FlatField(length = 8, dateTimeFormat = "yyyyMMdd")
    private LocalDate birthDate;
    
    @FlatField(length = 26, dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

} 