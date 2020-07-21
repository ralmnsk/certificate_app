package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.impl.Constants.*;

@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag, Integer> implements TagRepository {
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

    public List<String> findFrequentTag() {
        String sql = "select name from(\n" +
                "                        select count(id) as cnt,name from\n" +
                "                            (select tag.id,tag.name from tag\n" +
                "                             join cert_tag ct on tag.id = ct.tag_id\n" +
                "                             join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
                "                             join orders o on oc.order_id = o.id\n" +
                "                             join users u on o.user_id = u.id\n" +
                "                                where u.id =\n" +
                "\n" +
                "                                      (select uid from\n" +
                "                                          (select sum (tc.total_cost) as summa,tc.id as uid from\n" +
                "                                              (select orders.total_cost,u.id from orders join users u on orders.user_id = u.id) tc group by tc.id) as uid_summa\n" +
                "\n" +
                "                                       where summa =\n" +
                "\n" +
                "                                             (select max(summa) from\n" +
                "                                                 (select sum (tc.total_cost) as summa,tc.id as uid from\n" +
                "                                                     (select orders.total_cost,u.id from orders join users u on orders.user_id = u.id) tc group by tc.id)\n" +
                "                                                     s))\n" +
                "\n" +
                "                                ) as tg group by name) as t\n" +
                "\n" +
                "where cnt =\n" +
                "(select max(cnt) from\n" +
                "(select count(id) as cnt,name from\n" +
                "(select tag.id,tag.name from tag\n" +
                "    join cert_tag ct on tag.id = ct.tag_id\n" +
                "    join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
                "    join orders o on oc.order_id = o.id\n" +
                "    join users u on o.user_id = u.id\n" +
                "    where u.id=\n" +
                "\n" +
                "          (select uid from\n" +
                "              (select sum (tc.total_cost) as summa,tc.id as uid from\n" +
                "                  (select orders.total_cost,u.id from orders join users u on orders.user_id = u.id) tc group by tc.id) as uid_summa\n" +
                "\n" +
                "           where summa =\n" +
                "\n" +
                "                 (select max(summa) from\n" +
                "                     (select sum (tc.total_cost) as summa,tc.id as uid from\n" +
                "                         (select orders.total_cost,u.id from orders join users u on orders.user_id = u.id) tc group by tc.id)\n" +
                "                         s))\n" +
                "\n" +
                "\n" +
                "    ) as tg group by name) as t)";

        Query query = getEntityManager().createNativeQuery(sql);
        List<String> resultList = query.getResultList();

        return resultList;
    }

}
