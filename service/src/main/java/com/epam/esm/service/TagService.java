package com.epam.esm.service;


import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService<T> extends Service<T> {
    T getByName(String name);

    List<T> getAll();
}
