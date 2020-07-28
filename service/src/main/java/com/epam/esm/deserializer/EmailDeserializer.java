package com.epam.esm.deserializer;

import com.epam.esm.repository.exception.JsonParseCustomException;
import com.fasterxml.jackson.databind.util.StdConverter;

public class EmailDeserializer extends StdConverter<String, String> {
    private final static String REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public String convert(String value) {
        if (value.matches(REGEX)) {
            return value;
        }
        throw new JsonParseCustomException("Incorrect email address.");
    }

//    public static void main(String args[]){
//        EmailDeserializer emailDeserializer = new EmailDeserializer();
//        String convert = emailDeserializer.convert("i5-land@mail.ruruy");
//        System.out.println(convert);
//    }
}
