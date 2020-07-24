package com.epam.esm.deserializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * The type String to integer converter.
 * The StringToIntegerConverter converts an numeric string into Integer
 * or throws RuntimeException.
 */
@Slf4j
public class StringToIntegerConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(String value) {
        int num = 0;
        try {
            num = Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            log.error("String '" + value + "' to integer converting exception. Range 0 - 100000");
            throw new RuntimeException("String '" + value + "' to integer converting exception. Range 0 - 100000");
        }
        return num;

    }
}
