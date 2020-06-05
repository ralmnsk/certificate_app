package com.epam.esm.repository.tag;

import com.epam.esm.repository.Repository;

import java.util.List;

public interface TagRepository<T> extends Repository<T> {

    List<T> getAll();

    T getByName(String name);
}
