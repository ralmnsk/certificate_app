package com.epam.esm.web.page;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class TagPageBuilder extends AbstractPageBuilder {
    private final String EMPTY = "";
//    private TagService<TagDto, Integer> service;
//    private TagAssembler certificateAssembler;

    public TagPageBuilder(TagService<TagDto, Integer> service, TagAssembler certificateAssembler) {
        super(new HashSet<>(Arrays.asList("tagName")), service, certificateAssembler);
//        this.service = service;
//        this.certificateAssembler = certificateAssembler;
    }

//    public CustomPageDto<TagDto> build(FilterDto filterDto) {
//        CustomPageDto<TagDto> page = new CustomPageDto<>();
//        filterDto = validateFilter(filterDto);
//
//        filterDto = setSort(filterDto);
//        CollectionModel<TagDto> collectionModel = getCollectionModel(filterDto);
//
//        filterDto = service.getFilterDto();
//        page.setSize(filterDto.getSize());
//        page.setTotalElements(filterDto.getTotalElements());
//        page.setPage(filterDto.getPage());
//        page.setElements(collectionModel);
//        page.setTotalPage(filterDto.getTotalPages());
//
//        return page;
//    }
//
//    private CollectionModel<TagDto> getCollectionModel(FilterDto filterDto) {
//        return certificateAssembler.toCollectionModel(filterDto);
//    }


}