package com.epam.esm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CrudService<T, E> {

    Optional<T> save(T t);

    Optional<T> get(E id);

    Optional<T> update(T t);

    boolean delete(E id);

    Page<T> getAll(Pageable pageable);

}
