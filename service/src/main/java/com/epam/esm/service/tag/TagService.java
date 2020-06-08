package com.epam.esm.service.tag;


import com.epam.esm.service.Service;

import java.util.List;
import java.util.Optional;

public interface TagService<T> extends Service<T> {
    Optional<T> getByName(String name);

    List<T> getAll();
}
