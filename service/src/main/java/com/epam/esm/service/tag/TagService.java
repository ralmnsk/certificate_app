package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.filter.TagFilterDto;
import com.epam.esm.service.dto.wrapper.TagListWrapperDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService extends CrudService<TagDto, Integer> {

    Optional<TagDto> getByName(String name);

    void addTagToCertificate(Long certificateId, Set<Long> list);

    void deleteTagFromCertificate(Long certificateId, Set<Long> list);

    TagListWrapperDto getAll(TagFilterDto filterDto);

    List<String> findFrequentTag();

}
