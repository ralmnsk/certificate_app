package com.epam.esm.repository.tag;

import com.epam.esm.repository.Repository;

import java.util.List;

public interface TagRepository<T, E> extends Repository<T, E> {

    List<T> getAll();

}
