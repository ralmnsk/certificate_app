package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.filter.TagFilterDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.controller.TagController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler implements Assembler<Long, TagDto,TagFilterDto> {
    private TagService tagService;

    public TagAssembler(TagService tagService) {
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
    public CollectionModel<TagDto> toCollectionModel(TagFilterDto filter) {
        ListWrapperDto<TagDto, TagFilterDto> wrapperDto = tagService.getAll(filter);
        List<TagDto> tags = wrapperDto.getList();
        filter = wrapperDto.getFilterDto();

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
