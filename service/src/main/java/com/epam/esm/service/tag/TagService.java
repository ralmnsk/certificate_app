package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.FilterDto;

import java.util.Optional;

public interface TagService<T, E> extends CrudService<T, E> {

    Optional<T> getByName(String name);

//    Optional<T> createTagInOrder(Long certificateId, T tagDto);


}
