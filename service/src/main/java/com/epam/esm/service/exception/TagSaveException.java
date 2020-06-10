package com.epam.esm.service.exception;

import com.epam.esm.service.dto.TagDto;

public class TagSaveException extends RuntimeException {
    public TagSaveException(TagDto tagDto) {
        super("Certificate save exception: " + tagDto);
    }

    public TagSaveException(String message) {
        super("Certificate save exception: " + message);
    }
}
