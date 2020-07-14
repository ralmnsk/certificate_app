package com.epam.esm.repository.crud;

import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.AbstractFilter;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;


public interface TagRepository<T,E,F extends AbstractFilter> extends CrudRepository<T,E> {

    Optional<Tag> getByName(String name);

    ListWrapper<T,F> getAll(F filter);
}
