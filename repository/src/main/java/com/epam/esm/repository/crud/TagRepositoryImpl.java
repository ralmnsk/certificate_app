package com.epam.esm.repository.crud;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.ListWrapper;
import com.epam.esm.model.wrapper.TagListWrapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.crud.Constants.*;

@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag, Integer> implements TagRepository{
    private QueryBuilder<TagFilter> queryBuilder;

    public TagRepositoryImpl(QueryBuilder<TagFilter> queryBuilder) {
        super(Tag.class);
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Optional<Tag> getByName(String name) {
        Query query = getEntityManager()
                .createNativeQuery("select * from tag where tag.name = :name", Tag.class)
                .setParameter(NAME, name);
        Tag tag = (Tag) query.getSingleResult();
        if (tag != null) {
            return Optional.of(tag);
        }
        return Optional.empty();
    }

    @Override
    public TagListWrapper getAll(TagFilter tagFilter) {

        Query query = getEntityManager().createQuery("select distinct t from Tag t where t.name like :name order by t.name", Tag.class);
        query.setParameter(NAME, PERCENT_START + tagFilter.getTagName() + PERCENT_END);
        int pageNumber = tagFilter.getPage();
        int pageSize = tagFilter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Tag> tags = query.getResultList();

        Query queryTotal = getEntityManager().createQuery
                ("select distinct count(f.id) From Tag f where f.name like :name");
        queryTotal.setParameter(NAME, PERCENT_START + tagFilter.getTagName() + PERCENT_END);
        long countResult = (long) queryTotal.getSingleResult();

        tagFilter = queryBuilder.updateFilter(tagFilter, pageSize, countResult);
        TagListWrapper listWrapper = new TagListWrapper();
        listWrapper.setList(tags);
        listWrapper.setFilter(tagFilter);

        return listWrapper;
    }

}
