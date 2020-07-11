package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.controller.TagController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler implements Assembler<Long, TagDto> {
    private TagService<TagDto, Integer> tagService;

    public TagAssembler(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    @Override
    public TagDto assemble(Long tagId, TagDto tagDto) {
        int id = tagId.intValue();
        Link linkSelfTag = linkTo(methodOn(TagController.class).get(id)).withSelfRel();
        tagDto.add(linkSelfTag);

        return tagDto;
    }

    @Override
    public CollectionModel<TagDto> toCollectionModel(FilterDto filter) {
        List<TagDto> tags = tagService.getAll(filter);
        filter = tagService.getFilterDto();

        if (!tags.isEmpty()) {
            tags.forEach(c -> {
                Link selfLink = linkTo(methodOn(TagController.class).get(c.getId())).withSelfRel();
                c.add(selfLink);
            });
        }
        CollectionModel<TagDto> collection = CollectionModel.of(tags);

        return collection;
    }
}
