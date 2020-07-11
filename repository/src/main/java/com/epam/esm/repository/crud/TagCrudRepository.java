package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;


public interface TagCrudRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> getByName(String name);

    void removeFromRelationByTagId(Integer tagId);

    Filter getFilter();

}
