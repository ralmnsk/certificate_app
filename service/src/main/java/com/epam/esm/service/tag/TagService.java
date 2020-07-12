package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.IdDto;

import java.util.List;
import java.util.Optional;

public interface TagService<T, E> extends CrudService<T, E> {

    Optional<T> getByName(String name);

    void addTagToCertificate(Long certificateId, List<IdDto> list);

    void deleteTagFromCertificate(Long certificateId, List<IdDto> list);

}
