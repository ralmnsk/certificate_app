package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.AbstractFilter;
import com.epam.esm.model.filter.CertificateFilter;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

@Component
public class QueryBuilder<F extends AbstractFilter> {

    private static final String SELECT = " select distinct ";
    private static final String TAG = " tag.id, tag.deleted, tag.name ";
    private static final String CERTIFICATE = " certificate.id,certificate.creation,certificate.deleted,certificate.description,certificate.duration, certificate.modification,certificate.name,certificate.price ";
    private static final String ORDER = " orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
    private static final String USER = " users.id, users.deleted, users.login, users.name, users.password, users.role, users.surname ";

    private static final String TAG_TABLE = " tag ";
    private static final String CERTIFICATE_TABLE = " certificate ";
    private static final String ORDER_TABLE = " orders ";
    private static final String USER_TABLE = " users ";

    private static final String FROM = " from ";
    private static final String TAG_JOIN_CERTIFICATE = " join cert_tag ct on certificate.id = ct.certificate_id join tag on ct.tag_id = tag.id ";
    private static final String CERTIFICATE_JOIN_ORDER = " join order_certificate oc on certificate.id = oc.certificate_id join orders on oc.order_id = orders.id ";
    private static final String ORDER_JOIN_USER = " join users on orders.user_id = users.id ";

    private static final String WHERE = " where ";
    private static final String AND = " and ";

    private static final String TAG_NAME = " tag.name like :tagName ";
    private static final String CERTIFICATE_NAME = " certificate.name like :certificateName ";
    private static final String USER_SURNAME = " users.surname like :surname ";
    private static final String USER_NAME = " users.name like :userName ";

    private static final String TAG_ID = " tag.id = :tagId ";
    private static final String CERTIFICATE_ID = " certificate.id = :certificateId ";
    private static final String ORDER_ID = " orders.id = :orderId and orders.deleted = false";
    private static final String USER_ID = " users.id = :userId ";

    private static final String ORDER_BY = " order by  ";
    private static final String SELECT_COUNT = "select count(*) from (";
    private static final String SELECT_COUNT_END = " ) c ";
    private static final String EMPTY = "";
    private static final String COUNT = "count";
    private static final String PERCENT = "%";
    private static final String CERTIFICATE_NAME_PARAM = "certificateName";
    private static final String TAG_NAME_PARAM = "tagName";
    private static final String SURNAME_PARAM = "surname";
    private static final String USER_NAME_PARAM = "userName";
    private static final String TAG_ID_PARAM = "tagId";
    private static final String CERTIFICATE_ID_PARAM = "certificateId";
    private static final String ORDER_ID_PARAM = "orderId";
    private static final String USER_ID_PARAM = "userId";
    private static final String CERTIFICATE_DELETED_FALSE = " certificate.deleted = false and ";

    private HashSet<String> entityNameSet;

    private F filter;

    public StringBuilder assembleQlString(CertificateFilter certificateFilter, Class<?> clazz, String selecting) {//for class
        getEntityNameSet(certificateFilter);
        String tagCert = entityNameSet.contains(TAG_TABLE) ? TAG_JOIN_CERTIFICATE : EMPTY;
        String certOrder = entityNameSet.contains(ORDER_TABLE) ? CERTIFICATE_JOIN_ORDER : EMPTY;
        String orderUser = entityNameSet.contains(USER_TABLE) ? ORDER_JOIN_USER : EMPTY;

        String tagName = entityNameSet.contains(TAG_TABLE) ? TAG_NAME + AND : EMPTY;
        String userName = entityNameSet.contains(USER_TABLE) ? AND + USER_SURNAME + AND + USER_NAME : EMPTY;

        StringBuilder ql = (new StringBuilder(SELECT))
                .append(addObjectColumns(clazz))
                .append(FROM)
                .append(addObject(clazz))
                .append(tagCert)
                .append(certOrder)
                .append(orderUser)
                .append(WHERE)
                .append(CERTIFICATE_DELETED_FALSE)
                .append(tagName)
                .append(CERTIFICATE_NAME)
                .append(userName);

        addObjectId(certificateFilter, ql);

        ql = addSortToQueryString(certificateFilter, selecting, ql);

        if (selecting.equals(COUNT)) {
            ql = (new StringBuilder(SELECT_COUNT)).append(ql).append(SELECT_COUNT_END);

        }

        return ql;
    }

    private Set<String> getEntityNameSet(CertificateFilter certificateFilter) {
        entityNameSet = new HashSet<>();
        if (certificateFilter.getTagId() != null && certificateFilter.getTagId() > 0) {
            entityNameSet.add(TAG_TABLE);
        }
        if (certificateFilter.getCertificateId() != null && certificateFilter.getCertificateId() > 0) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (certificateFilter.getOrderId() != null && certificateFilter.getOrderId() > 0) {
            entityNameSet.add(ORDER_TABLE);
        }
        if (certificateFilter.getUserId() != null && certificateFilter.getUserId() > 0) {
            entityNameSet.add(USER_TABLE);
        }
        if (certificateFilter.getCertificateName() != null && !certificateFilter.getCertificateName().isEmpty()) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (certificateFilter.getTagName() != null && !certificateFilter.getTagName().isEmpty()) {
            entityNameSet.add(TAG_TABLE);
        }
        if (certificateFilter.getUserSurname() != null && !certificateFilter.getUserSurname().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }
        if (certificateFilter.getUserName() != null && !certificateFilter.getUserName().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }

        return this.entityNameSet;
    }

    private StringBuilder addObjectId(CertificateFilter certificateFilter, StringBuilder ql) {
        if (certificateFilter.getTagId() != null && certificateFilter.getTagId() > 0) {
            ql.append(AND).append(TAG_ID);
        }
        if (certificateFilter.getCertificateId() != null && certificateFilter.getCertificateId() > 0) {
            ql.append(AND).append(CERTIFICATE_ID);
        }
        if (certificateFilter.getOrderId() != null && certificateFilter.getOrderId() > 0) {
            ql.append(AND).append(ORDER_ID);
        }
        if (certificateFilter.getUserId() != null && certificateFilter.getUserId() > 0) {
            ql.append(AND).append(USER_ID);
        }
        return ql;
    }


    public F updateFilter(F filter, int pageSize, long countResult) {
        filter.setTotalPages((countResult / pageSize));
        if (countResult % pageSize != 0) {
            filter.setTotalPages(((int) (countResult / pageSize) + 1));
        }
        filter.setTotalElements(countResult);

        return filter;
    }

    private String addObjectColumns(Class<?> clazz) {
        if (clazz.equals(Tag.class)) {
            return TAG;
        }
        if (clazz.equals(Certificate.class)) {
            return CERTIFICATE;
        }
        if (clazz.equals(Order.class)) {
            return ORDER;
        }
        if (clazz.equals(User.class)) {
            return USER;
        }
        return TAG;
    }

    private String addObject(Class<?> clazz) {
        if (clazz.equals(Tag.class)) {
            return TAG_TABLE;
        }
        if (clazz.equals(Certificate.class)) {
            return CERTIFICATE_TABLE;
        }
        if (clazz.equals(Order.class)) {
            return ORDER_TABLE;
        }
        if (clazz.equals(User.class)) {
            return USER_TABLE;
        }
        return TAG_TABLE;
    }


    public StringBuilder addSortToQueryString(AbstractFilter abstractFilter, String selecting, StringBuilder ql) {
        if (!selecting.equals(COUNT) && abstractFilter.getFilterSort() != null && !abstractFilter.getFilterSort().getFilterOrders().isEmpty()) {
            String str = abstractFilter
                    .getFilterSort()
                    .getFilterOrders()
                    .stream()
                    .map(o -> o.getParameter() + " " + o.getFilterDirection()).reduce("", (a, b) -> " " + a + " " + b + ",");
            str = str.substring(0, str.length() - 1);
            ql.append(ORDER_BY).append(str);
        }
        return ql;
    }

    public void setParameters(CertificateFilter certificateFilter, Query query) {
        query.setParameter(CERTIFICATE_NAME_PARAM, PERCENT + certificateFilter.getCertificateName() + PERCENT);
        if (entityNameSet.contains(TAG_TABLE)) {
            query.setParameter(TAG_NAME_PARAM, PERCENT + certificateFilter.getTagName() + PERCENT);
        }
        if (entityNameSet.contains(USER_TABLE)) {
            query.setParameter(SURNAME_PARAM, PERCENT + certificateFilter.getUserSurname() + PERCENT);
            query.setParameter(USER_NAME_PARAM, PERCENT + certificateFilter.getUserName() + PERCENT);
        }

        if (certificateFilter.getTagId() != null && certificateFilter.getTagId() > 0) {
            query.setParameter(TAG_ID_PARAM, certificateFilter.getTagId());
        }

        if (certificateFilter.getCertificateId() != null && certificateFilter.getCertificateId() > 0) {
            query.setParameter(CERTIFICATE_ID_PARAM, certificateFilter.getCertificateId());
        }

        if (certificateFilter.getOrderId() != null && certificateFilter.getOrderId() > 0) {
            query.setParameter(ORDER_ID_PARAM, certificateFilter.getOrderId());
        }

        if (certificateFilter.getUserId() != null && certificateFilter.getUserId() > 0) {
            query.setParameter(USER_ID_PARAM, certificateFilter.getUserId());
        }
    }

}
