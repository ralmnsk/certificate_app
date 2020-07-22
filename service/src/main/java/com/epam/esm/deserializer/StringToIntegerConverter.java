package com.epam.esm.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * The type String to integer converter.
 * The StringToIntegerConverter converts an numeric string into Integer
 * or throws RuntimeException.
 */
public class StringToIntegerConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(String value) {
        int num = 0;
        try {
            num = Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("String '" + value + "' to integer converting exception. Range 0 - 100000");
        }
        return num;

    }
}
