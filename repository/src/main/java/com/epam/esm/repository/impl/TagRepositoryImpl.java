package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;


@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag, Integer> implements TagRepository {
    public final static String PERCENT = "%";
    private final String NAME = "name";
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
        query.setParameter(NAME, PERCENT + tagFilter.getTagName() + PERCENT);
        int pageNumber = tagFilter.getPage();
        int pageSize = tagFilter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Tag> tags = query.getResultList();

        Query queryTotal = getEntityManager().createQuery
                ("select distinct count(f.id) From Tag f where f.name like :name");
        queryTotal.setParameter(NAME, PERCENT + tagFilter.getTagName() + PERCENT);
        long countResult = (long) queryTotal.getSingleResult();

        tagFilter = queryBuilder.updateFilter(tagFilter, pageSize, countResult);
        TagListWrapper listWrapper = new TagListWrapper();
        listWrapper.setList(tags);
        listWrapper.setFilter(tagFilter);

        return listWrapper;
    }

    public List<String> findTopTag() {
        String sql = "select name\n" +
                "from (select count(id) as cnt, name\n" +
                "      from (select tag.id, tag.name\n" +
                "            from tag\n" +
                "                     join cert_tag ct on tag.id = ct.tag_id\n" +
                "                     join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
                "                     join orders o on oc.order_id = o.id\n" +
                "                     join users u on o.user_id = u.id\n" +
                "            where u.id =\n" +

                "                  (select uid\n" +
                "                   from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
                "                         from orders o\n" +
                "                                  join users u on o.user_id = u.id\n" +
                "                         where o.deleted = false\n" +
                "                           and u.deleted = false\n" +
                "                         group by o.user_Id) as uid_summa\n" +
                "                   where summa =\n" +
                "                         (select max(summa)\n" +
                "                          from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
                "                                from orders o\n" +
                "                                         join users u on o.user_id = u.id\n" +
                "                                where o.deleted = false\n" +
                "                                  and u.deleted = false\n" +
                "                                group by o.user_Id) s))\n" +

                "           ) as tg\n" +
                "      group by name) as t\n" +
                "where cnt =\n" +
                "      (select max(cnt)\n" +
                "       from (select count(id) as cnt, name\n" +
                "             from (select tag.id, tag.name\n" +
                "                   from tag\n" +
                "                            join cert_tag ct on tag.id = ct.tag_id\n" +
                "                            join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
                "                            join orders o on oc.order_id = o.id\n" +
                "                            join users u on o.user_id = u.id\n" +
                "                   where u.id =\n" +

                "                         (select uid\n" +
                "                          from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
                "                                from orders o\n" +
                "                                         join users u on o.user_id = u.id\n" +
                "                                where o.deleted = false\n" +
                "                                  and u.deleted = false\n" +
                "                                group by o.user_Id) as uid_summa\n" +
                "                          where summa =\n" +
                "                                (select max(summa)\n" +
                "                                 from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
                "                                       from orders o\n" +
                "                                                join users u on o.user_id = u.id\n" +
                "                                       where o.deleted = false\n" +
                "                                         and u.deleted = false\n" +
                "                                       group by o.user_Id) s))\n" +

                "                  ) as tg\n" +
                "             group by name) as t)";

        Query query = getEntityManager().createNativeQuery(sql);
        List<String> resultList = query.getResultList();

        return resultList;
    }

}
