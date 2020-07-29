package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;

import java.util.List;
import java.util.Optional;


public interface TagRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> getByName(String name);

    TagListWrapper getAll(TagFilter filter);

    List<Integer> findTopTag();
}