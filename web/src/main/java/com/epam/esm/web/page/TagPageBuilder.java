package com.epam.esm.web.page;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.filter.TagFilterDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class TagPageBuilder extends AbstractPageBuilder<TagDto, TagService<TagDto, Integer, TagFilterDto>, TagAssembler, TagFilterDto> {
    private final String EMPTY = "";

    public TagPageBuilder(TagService<TagDto, Integer, TagFilterDto> service, TagAssembler certificateAssembler) {
        super(new HashSet<>(Arrays.asList("tagName")), service, certificateAssembler);
    }

    public CustomPageDto<TagDto> build(TagFilterDto filterDto) {
        CustomPageDto<TagDto> page = new CustomPageDto<>();
        filterDto = validateFilter(filterDto);

        filterDto = setSort(filterDto);

        ListWrapperDto<TagDto, TagFilterDto> listWrapperDto = getService().getAll(filterDto);
        CollectionModel<TagDto> collectionModel = getCollectionModel(filterDto);

        filterDto = listWrapperDto.getFilterDto();
        page.setSize(filterDto.getSize());
        page.setTotalElements(filterDto.getTotalElements());
        page.setPage(filterDto.getPage());
        page.setElements(collectionModel);
        page.setTotalPage(filterDto.getTotalPages());

        return page;
    }

}