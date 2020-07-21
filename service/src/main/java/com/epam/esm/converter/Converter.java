package com.epam.esm.converter;

public interface Converter<D, E> {
    E toEntity(D d);

    D toDto(E e);
}