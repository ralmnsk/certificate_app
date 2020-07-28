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
        List<Tag> tags = getList(tagFilter);
        tagFilter = setCountResult(tagFilter);

        TagListWrapper listWrapper = new TagListWrapper();
        listWrapper.setList(tags);
        listWrapper.setFilter(tagFilter);

        return listWrapper;
    }

    public List<Integer> findTopTag() {

        String sumString = "select max(summa) from (select sum(orders.total_cost) as summa,user_id as uid from orders join users u on orders.user_id = u.id where orders.deleted = false and u.deleted = false group by user_id) su"; //if user delted then orders are all deleted
        Query sumQuery = getEntityManager().createNativeQuery(sumString);
        BigDecimal summa = (BigDecimal) sumQuery.getSingleResult();
        String uidString = "select uid from (select sum(orders.total_cost) as summa,user_id as uid from orders join users u on orders.user_id=u.id where orders.deleted = false and u.deleted = false group by user_id) su where summa = :summa";
        Query uidQuery = getEntityManager().createNativeQuery(uidString);
        uidQuery.setParameter("summa", summa);
        List<BigInteger> uids = uidQuery.getResultList();
        String sql = "select tid from " +
                "                (select count(cert_tag.tag_id) as cnt,cert_tag.tag_id as tid from cert_tag join certificate c on cert_tag.certificate_id = c.id " +
                "                join order_certificate oc on c.id = oc.certificate_id " +
                "                join orders o on oc.order_id = o.id " +
                "                join users u on o.user_id = u.id " +
                "                where c.deleted = false and o.deleted = false and u.deleted = false and o.user_id = :uid group by tid) as b " +
                "                where cnt= " +
                "                (select max(cnt) from " +
                "                    (select count(cert_tag.tag_id) as cnt,cert_tag.tag_id as tid from cert_tag join certificate c on cert_tag.certificate_id = c.id " +
                "                join order_certificate oc on c.id = oc.certificate_id " +
                "                join orders o on oc.order_id = o.id " +
                "                join users u on o.user_id = u.id " +
                "                where c.deleted = false and o.deleted = false and u.deleted = false and o.user_id = :uid group by tid) as b )";
        Query query = getEntityManager().createNativeQuery(sql);

        List<Integer> resultList = new ArrayList<>();
        uids.forEach(id -> {
            query.setParameter(UID, id);
            List<Integer> result = query.getResultList();
            result.forEach(r -> resultList.add(r));
        });

        return resultList;
    }

    private List<Tag> getList(TagFilter tagFilter) {
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

        return tags;
    }

    private TagFilter setCountResult(TagFilter tagFilter) {
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
        int pageSize = tagFilter.getSize();
        tagFilter = queryBuilder.updateFilter(tagFilter, pageSize, countResult.longValue());

        return tagFilter;
    }

}
