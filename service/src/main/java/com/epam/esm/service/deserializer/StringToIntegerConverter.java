package com.epam.esm.service.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * The type String to integer converter.
 */
public class StringToIntegerConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(String value) {
        int num = 0;
        try {
            num = Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("String '" + value + "' to integer converting exception.");
        }
        return num;

    }
}
