package com.epam.esm.repository.crud;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;


public interface TagRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> getByName(String name);

    TagListWrapper getAll(TagFilter filter);
}
