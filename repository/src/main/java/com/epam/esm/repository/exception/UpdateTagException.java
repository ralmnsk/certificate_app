package com.epam.esm.repository.exception;

import com.epam.esm.model.Tag;

public class UpdateTagException extends Exception {
    public UpdateTagException(String message){
        super("Update tag exception: "+message);
    }
}
