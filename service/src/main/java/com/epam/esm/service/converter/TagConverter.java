package com.epam.esm.service.converter;

import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * The type Tag converter.
 * The TagConverter converts {@link TagDto} into {@link Tag}
 * and vice versa.
 */
@Component
public class TagConverter implements Converter<TagDto, Tag> {
    private ModelMapper modelMapper;

    /**
     * Instantiates a new Tag converter.
     *
     * @param modelMapper the model mapper is attached automatically with Spring.
     */
    public TagConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Tag toEntity(TagDto tagDto) {
        if (tagDto != null) {
            return modelMapper.map(tagDto, Tag.class);
        }
        return null;
    }


    @Override
    public TagDto toDto(Tag tag) {
        if (tag != null) {
            return modelMapper.map(tag, TagDto.class);
        }
        return null;
    }
}
