package com.epam.esm.service;

public interface Service<T> {
    boolean save(T t);
    T get(Long id);
    boolean update(T t);
    boolean delete(Long id);
}
