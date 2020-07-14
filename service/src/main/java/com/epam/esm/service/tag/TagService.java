package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;

import java.util.Optional;
import java.util.Set;

public interface TagService<T, E, F extends AbstractFilterDto> extends CrudService<T, E> {

    Optional<TagDto> getByName(String name);

    void addTagToCertificate(Long certificateId, Set<Long> list);

    void deleteTagFromCertificate(Long certificateId, Set<Long> list);

    ListWrapperDto<T, F> getAll(F filterDto);


}
