package com.epam.esm.service;

import com.epam.esm.service.dto.FilterDto;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, E> {

    Optional<T> save(T t);

    Optional<T> get(E id);

    Optional<T> update(T t);

    boolean delete(E id);

    List<T> getAll(FilterDto filterDto);

    FilterDto getFilterDto();
}
