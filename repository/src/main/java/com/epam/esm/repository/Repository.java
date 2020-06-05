package com.epam.esm.repository;

public interface Repository<T> {

    boolean save(T t);

    T get(Long id);

    boolean update(T t);

    boolean delete(Long id);
}
