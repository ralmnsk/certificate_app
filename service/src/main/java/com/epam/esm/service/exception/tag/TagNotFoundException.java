package com.epam.esm.service.exception.tag;

import com.epam.esm.service.exception.GeneralException;

public class TagNotFoundException extends GeneralException { //https://spring.io/guides/tutorials/rest/
    public TagNotFoundException(Long id){
        super("Could not find tag " +id);
    }
}
