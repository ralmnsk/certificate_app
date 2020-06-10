package com.epam.esm.service.tag;


import com.epam.esm.service.Service;

import java.util.List;

public interface TagService<T> extends Service<T> {

    List<T> getAll();
}
