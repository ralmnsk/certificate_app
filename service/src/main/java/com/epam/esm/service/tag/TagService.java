package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;

import java.util.Optional;

public interface TagService<T, E> extends CrudService<T, E> {

//    Page<T> getAllByCertificateId(Long certificateId, Pageable pageable);

    Optional<T> getByName(String name);

    Optional<T> createTagInOrder(Long certificateId, T tagDto);
}
