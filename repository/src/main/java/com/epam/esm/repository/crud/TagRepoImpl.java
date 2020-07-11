package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
@Getter
public class TagRepoImpl extends AbstractRepo<Tag, Integer> implements TagCrudRepository {

    public TagRepoImpl() {
        super(Tag.class);
    }

    @Override
    public Optional<Tag> getByName(String name) {
        Query query = getEntityManager()
                .createNativeQuery("select * from tag where tag.name = :name", Tag.class)
                .setParameter("name", name);
        Tag tag = (Tag) query.getSingleResult();
        if (tag != null) {
            return Optional.of(tag);
        }
        return Optional.empty();
    }

    @Override
    public void removeFromRelationByTagId(Integer tagId) {
        EntityManager entityManager;
        Query query = getEntityManager()
                .createNativeQuery("delete from cert_tag where tag_id = :tagId")
                .setParameter("tagId", tagId);
        query.executeUpdate();
    }

    @Override
    public List<Tag> getAll(Filter filter) {
        Query query = getEntityManager().createQuery("select t from Tag t where t.name like :name order by t.name", Tag.class);
        query.setParameter("name", filter.getTagName());
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Tag> tags = query.getResultList();

        Query queryTotal = getEntityManager().createQuery
                ("select count(f.id) From Tag f where f.name like :name");
        queryTotal.setParameter("name", filter.getTagName());
        long countResult = (long) queryTotal.getSingleResult();

        updateFilter(filter, pageSize, countResult);

        return tags;
    }

}
