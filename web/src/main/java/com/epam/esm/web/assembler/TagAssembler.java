package com.epam.esm.web.assembler;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.TagService;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.TagController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler implements Assembler<Long, TagDto, TagFilterDto> {
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
        addNextPrevious(collection, filter);

        return collection;
    }

    private void addNextPrevious(CollectionModel<TagDto> collectionModel, TagFilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(CertificateController.class)
                    .getAll(
                            filter.getTagName(),
                            filter.getCertificateName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams()
                    )).withRel("order_id_" + filter.getOrderId() + "_certificates_previous_page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(CertificateController.class)
                    .getAll(
                            filter.getTagName(),
                            filter.getCertificateName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams()
                    )).withRel("order_id_" + filter.getOrderId() + "_certificates_next_page");
            collectionModel.add(link);
        }
    }
}
