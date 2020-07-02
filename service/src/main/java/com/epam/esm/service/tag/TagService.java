package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagService<T, E> extends CrudService<T, E> {

    Page<T> getAllByCertificateId(Long certificateId, Pageable pageable);

    Optional<T> getByName(String name);
}
