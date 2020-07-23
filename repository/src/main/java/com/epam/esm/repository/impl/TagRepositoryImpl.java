package com.epam.esm.repository.impl;

import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.TagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag, Integer> implements TagRepository {
    public final static String PERCENT = "%";
    private final static String NAME = "name";
    private final static String SELECT_BY_NAME = " select * from tag where tag.name = :name ";
    private final static String SELECT_FROM_TAG = "select distinct tag.id,tag.name,tag.deleted from tag ";
    private final static String JOIN_CERT_TAG = " join cert_tag on tag.id = cert_tag.tag_id ";
    private final static String WHERE_TAG_NAME = " where tag.name like :name and tag.deleted = false";

    private final static String CERTIFICATE_ID = " and cert_tag.certificate_id = :certificateId ";
    private final static String ORDER_BY = " order by tag.name ";
    private final static String SELECT_COUNT = " select distinct count(tag.id) from tag ";
    private final static String UID = "uid";
    private final static String CERTIFICATE_ID_PARAM = "certificateId";

    private QueryBuilder<TagFilter> queryBuilder;

    public TagRepositoryImpl(QueryBuilder<TagFilter> queryBuilder) {
        super(Tag.class);
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Optional<Tag> getByName(String name) {
        Query query = getEntityManager()
                .createNativeQuery(SELECT_BY_NAME, Tag.class)
                .setParameter(NAME, name);
        Tag tag = (Tag) query.getSingleResult();
        if (tag != null) {
            return Optional.of(tag);
        }
        return Optional.empty();
    }

    @Override
    public TagListWrapper getAll(TagFilter tagFilter) {
        StringBuilder sql = new StringBuilder(SELECT_FROM_TAG);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            sql.append(JOIN_CERT_TAG);
        }
        sql.append(WHERE_TAG_NAME);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            sql.append(CERTIFICATE_ID);
        }
        sql.append(ORDER_BY);
        Query query = getEntityManager().createNativeQuery(sql.toString(), Tag.class);
        query.setParameter(NAME, PERCENT + tagFilter.getTagName() + PERCENT);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            query.setParameter(CERTIFICATE_ID_PARAM, tagFilter.getCertificateId());
        }
        int pageNumber = tagFilter.getPage();
        int pageSize = tagFilter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Tag> tags = query.getResultList();

        StringBuilder sqlTotal = new StringBuilder(SELECT_COUNT);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            sqlTotal.append(JOIN_CERT_TAG);
        }
        sqlTotal.append(WHERE_TAG_NAME);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            sqlTotal.append(CERTIFICATE_ID);
        }

        Query queryTotal = getEntityManager().createNativeQuery(sqlTotal.toString());
        queryTotal.setParameter(NAME, PERCENT + tagFilter.getTagName() + PERCENT);
        if (tagFilter.getCertificateId() != null && tagFilter.getCertificateId() > 0) {
            queryTotal.setParameter(CERTIFICATE_ID_PARAM, tagFilter.getCertificateId());
        }
        BigInteger countResult = (BigInteger) queryTotal.getSingleResult();

        tagFilter = queryBuilder.updateFilter(tagFilter, pageSize, countResult.longValue());
        TagListWrapper listWrapper = new TagListWrapper();
        listWrapper.setList(tags);
        listWrapper.setFilter(tagFilter);

        return listWrapper;
    }

    public List<String> findTopTag() {
//        String sql =
//        "select name\n" +
//                "from (select count(id) as cnt, name\n" +
//                "      from (select tag.id, tag.name\n" +
//                "            from tag\n" +
//                "                     join cert_tag ct on tag.id = ct.tag_id\n" +
//                "                     join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
//                "                     join orders o on oc.order_id = o.id\n" +
//                "                     join users u on o.user_id = u.id\n" +
//                "            where u.id =\n" +
//
//                "                  (select uid\n" +
//                "                   from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
//                "                         from orders o\n" +
//                "                                  join users u on o.user_id = u.id\n" +
//                "                         where o.deleted = false\n" +
//                "                           and u.deleted = false\n" +
//                "                         group by o.user_Id) as uid_summa\n" +
//                "                   where summa =\n" +
//                "                         (select max(summa)\n" +
//                "                          from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
//                "                                from orders o\n" +
//                "                                         join users u on o.user_id = u.id\n" +
//                "                                where o.deleted = false\n" +
//                "                                  and u.deleted = false\n" +
//                "                                group by o.user_Id) s))\n" +
//
//                "           ) as tg\n" +
//                "      group by name) as t\n" +
//                "where cnt =\n" +
//                "      (select max(cnt)\n" +
//                "       from (select count(id) as cnt, name\n" +
//                "             from (select tag.id, tag.name\n" +
//                "                   from tag\n" +
//                "                            join cert_tag ct on tag.id = ct.tag_id\n" +
//                "                            join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
//                "                            join orders o on oc.order_id = o.id\n" +
//                "                            join users u on o.user_id = u.id\n" +
//                "                   where u.id =\n" +
//
//                "                         (select uid\n" +
//                "                          from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
//                "                                from orders o\n" +
//                "                                         join users u on o.user_id = u.id\n" +
//                "                                where o.deleted = false\n" +
//                "                                  and u.deleted = false\n" +
//                "                                group by o.user_Id) as uid_summa\n" +
//                "                          where summa =\n" +
//                "                                (select max(summa)\n" +
//                "                                 from (select sum(o.total_cost) as summa, o.user_Id as uid\n" +
//                "                                       from orders o\n" +
//                "                                                join users u on o.user_id = u.id\n" +
//                "                                       where o.deleted = false\n" +
//                "                                         and u.deleted = false\n" +
//                "                                       group by o.user_Id) s))\n" +
//
//                "                  ) as tg\n" +
//                "             group by name) as t)";
        String sumString = "select max(summa) from (select sum(o.total_cost) as summa, o.user_Id as uid " +
                " from orders o join users u on o.user_id = u.id " +
                " where o.deleted = false and u.deleted = false group by o.user_Id) s";
//        Query query = getEntityManager().createNativeQuery(sql);
        Query sumQuery = getEntityManager().createNativeQuery(sumString);
//        List<String> resultList = query.getResultList();
        BigDecimal summa = (BigDecimal) sumQuery.getSingleResult();
        String uidString = "select uid from (select sum(o.total_cost) as summa, o.user_Id as uid " +
                " from orders o join users u on o.user_id = u.id " +
                "                                where o.deleted = false " +
                "                                  and u.deleted = false " +
                "                                group by o.user_Id) as uid_summa " +
                "                          where summa = :summa";
        Query uidQuery = getEntityManager().createNativeQuery(uidString);
        uidQuery.setParameter("summa", summa);
        List<BigInteger> uids = uidQuery.getResultList();
        String sql =
                "select name\n" +
                        "from (select count(id) as cnt, name\n" +
                        "      from (select tag.id, tag.name\n" +
                        "            from tag\n" +
                        "                     join cert_tag ct on tag.id = ct.tag_id\n" +
                        "                     join order_certificate oc on ct.certificate_id = oc.certificate_id\n" +
                        "                     join orders o on oc.order_id = o.id\n" +
                        "                     join users u on o.user_id = u.id\n" +
                        "            where u.id =  :uid\n" +
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
                        "                   where u.id = :uid\n" +
                        "                  ) as tg\n" +
                        "             group by name) as t)";
        List<String> resultList = new ArrayList<>();
        uids.forEach(id -> {
            Query query = getEntityManager().createNativeQuery(sql);
            query.setParameter(UID, id);
            List<String> result = query.getResultList();
            result.forEach(r -> resultList.add(r));
        });

        return resultList;
    }

}
