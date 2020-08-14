package com.epam.esm.deserializer;

import com.epam.esm.repository.exception.JsonParseCustomException;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToIntegerConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(String value) {
        int num = 0;
        try {
            num = Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            log.error("String '" + value + "' to integer converting exception. Range 0 - 100000");
            throw new JsonParseCustomException("String '" + value + "' to integer converting exception. Range 0 - 100000");
        }
        return num;

    }
}
