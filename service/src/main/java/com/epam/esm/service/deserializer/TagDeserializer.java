package com.epam.esm.service.deserializer;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.JsonDeserializationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.tag.TagService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
//@Component
public class TagDeserializer extends StdDeserializer<TagDto> {
    private TagService<TagDto, Integer> tagService;

    public TagDeserializer() {
        this(null);

    }

    public TagDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    public void setTagService(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    @Override
    public TagDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return deserializeTagNode(node);
    }

    public TagDto deserializeTagNode(JsonNode node) {
        Integer id = null;
        String name = null;

        try {
            id = (Integer) ((IntNode) node.get("id")).numberValue();
            if (id < 1) {
                throw new JsonDeserializationException("id must be more then 0");
            }
            Optional<TagDto> tagDtoOptional = tagService.get(id);
            if (tagDtoOptional.isPresent()) {
                return tagDtoOptional.get();
            } else {
                throw new NotFoundException(id);
            }
        } catch (Exception e) {
            if (e.getClass().equals(NotFoundException.class)) {
                throw new NotFoundException(id);
            }
            log.warn("Certificate exception, id:" + e.getMessage());
        }

        try {
            name = node.get("name").asText();
            if (name.length() < 2 || name.length() > 128) {
                throw new JsonDeserializationException("Name must be between 2 and 128 characters");
            }
            Optional<TagDto> byNameOptional = tagService.getByName(name);
            if (byNameOptional.isPresent()) {
                return byNameOptional.get();
            } else {
                TagDto t = new TagDto();
                t.setName(name);
                Optional<TagDto> save = tagService.save(t);
                if (save.isPresent()) {
                    return save.get();
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        if (name == null) {
            throw new JsonDeserializationException("Tag id and name are inconsistent");
        }

        TagDto tagDto = new TagDto();
        tagDto.setId(id);
        tagDto.setName(name);

        return tagDto;
    }
}
