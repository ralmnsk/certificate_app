package com.epam.esm.repository;

import com.epam.esm.model.Filter;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, E> {

    Optional<T> save(T t);

    Optional<T> get(E id);

    Optional<T> update(T t);

    boolean delete(E id);

    List<T> getAll(Filter filter);

    Filter getFilter();

}