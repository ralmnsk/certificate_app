package com.epam.esm.service;

import java.util.Optional;

public interface CrudService<T, E> {

    Optional<T> save(T t);

    Optional<T> get(E id);

    Optional<T> update(T t);

    boolean delete(E id);

}
