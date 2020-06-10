package com.epam.esm.service.exception;

import com.epam.esm.service.dto.TagDto;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException() {
        super("Tag already exists");
    }
    public TagAlreadyExistsException(TagDto tagDto) {
        super("Tag already exists:"+tagDto);
    }
}
