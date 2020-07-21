package com.epam.esm.service;


import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.dto.wrapper.TagListWrapperDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService extends CrudService<TagDto, Integer> {

    Optional<TagDto> getByName(String name);

    void addTagToCertificate(Long certificateId, Set<Long> list);

    void deleteTagFromCertificate(Long certificateId, Set<Long> list);

    TagListWrapperDto getAll(TagFilterDto filterDto);

    List<String> findTopTag();

}
