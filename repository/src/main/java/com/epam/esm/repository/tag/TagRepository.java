package com.epam.esm.repository.tag;

import com.epam.esm.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TagRepository<T, E> extends Repository<T, E> {

    List<T> getAll();

    Optional<T> getByName(String name);
}
