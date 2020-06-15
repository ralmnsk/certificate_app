package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;

import java.util.List;
import java.util.Optional;

public interface TagService<T, E> extends CrudService<T, E> {

    List<T> getAll();

    Optional<T> getByName(String name);
}
