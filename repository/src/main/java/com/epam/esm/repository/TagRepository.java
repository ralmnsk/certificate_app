package com.epam.esm.repository;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagRepository<T> extends Repository<T> {
    List<Tag> getAll();

    Tag getByName(String name);
}
