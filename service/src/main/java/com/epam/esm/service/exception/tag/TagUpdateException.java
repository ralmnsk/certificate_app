package com.epam.esm.service.exception.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.GeneralException;

public class TagUpdateException extends GeneralException {
    public TagUpdateException(){
        super("This feature is not implemented");
    }
}
