package com.epam.esm.repository;

import java.util.Optional;

public interface Repository<T,E> {

    Optional<T> save(T t);

    Optional<T> get(E id);

    Optional<T> update(T t);

    boolean delete(E id);
}
