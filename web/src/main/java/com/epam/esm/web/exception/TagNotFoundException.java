package com.epam.esm.web.exception;

public class TagNotFoundException extends RuntimeException { //https://spring.io/guides/tutorials/rest/
    public TagNotFoundException(Long id){
        super("Could not find tag " +id);
    }
}
