package com.epam.esm.service.exception.tag;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.GeneralException;
import org.springframework.dao.DuplicateKeyException;

public class TagSaveException extends GeneralException {
    public TagSaveException(TagDto tagDto) {
        super("Certificate save exception: " + tagDto);
    }

    public TagSaveException(String message) {
        super("Certificate save exception: " + message);
    }

    public TagSaveException(String s, TagDto tagDto, DuplicateKeyException e) {
        super(s + tagDto + e);
    }

    public TagSaveException(TagDto tagDto, NullPointerException e) {
        super("Certificate save exception: " + tagDto + " " + e);
    }
}
