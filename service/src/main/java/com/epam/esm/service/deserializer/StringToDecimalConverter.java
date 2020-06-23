package com.epam.esm.service.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

/**
 * The type String to decimal converter.
 * The StringToDecimalConverter converts an numeric String into BigDecimal
 * or throws RuntimeException.
 */
public class StringToDecimalConverter extends StdConverter<String, BigDecimal> {
    @Override
    public BigDecimal convert(String value) {
        BigDecimal num = new BigDecimal(0);
        try {
            num = new BigDecimal(value.trim());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("String '" + value + "' to decimal converting exception.");
        }
        return num;

    }
}
