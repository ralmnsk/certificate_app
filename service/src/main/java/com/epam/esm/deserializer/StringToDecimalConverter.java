package com.epam.esm.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringToDecimalConverter extends StdConverter<String, BigDecimal> {
    @Override
    public BigDecimal convert(String value) {
        BigDecimal num = new BigDecimal(0);
        try {
            num = new BigDecimal(value.trim());
            num = num.setScale(2, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("String '" + value + "' to decimal converting exception.");
        }
        return num;

    }
}
