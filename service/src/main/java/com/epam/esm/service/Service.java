package com.epam.esm.service;

import java.util.Optional;

public interface Service<T> {
    Optional<T> save(T t);
    Optional<T> get(Long id);
    Optional<T> update(T t);
    boolean delete(Long id);

}
