package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.wrapper.CertificateListWrapper;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class CertificateRepositoryImpl extends AbstractRepository<Certificate, Long> implements CertificateRepository {
    private static final  String SELECT = "select";
    private static final  String COUNT = "count";
    private static final  String COUNT_OF_ORDERS_BY_CERTIFICATE_ID = "select count(orders.id) from orders join order_certificate oc on orders.id = oc.order_id where oc.certificate_id = :certificateId ";
    private QueryBuilder<CertificateFilter> builder;

    public CertificateRepositoryImpl(QueryBuilder<CertificateFilter> builder) {
        super(Certificate.class);
        this.builder = builder;
    }

    @Override
    public CertificateListWrapper getAll(CertificateFilter filter) {
        List<Certificate> certificates = getList(filter);
        filter = setCountResult(filter);

        CertificateListWrapper wrapper = new CertificateListWrapper();
        wrapper.setList(certificates);
        wrapper.setFilter(filter);

        return wrapper;
    }

    private List<Certificate> getList(CertificateFilter filter) {
        String ql = builder.assembleQlString(filter, Certificate.class, SELECT).toString();
        Query query = getEntityManager().createNativeQuery(ql, Certificate.class);
        builder.setParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);

        return  query.getResultList();
    }

    private CertificateFilter setCountResult(CertificateFilter filter) {
        Query queryTotal = getEntityManager().createNativeQuery
                (builder.assembleQlString(filter, Certificate.class, COUNT).toString());
        builder.setParameters(filter, queryTotal);
        long countResult = Long.parseLong(queryTotal.getSingleResult().toString());
        int pageSize = filter.getSize();
        filter = builder.updateFilter(filter, pageSize, countResult);

        return filter;
    }

    @Override
    public Long getCountOrdersByCertificateId(Long certificateId) {
        Query query = getEntityManager().createNativeQuery(COUNT_OF_ORDERS_BY_CERTIFICATE_ID);
        query.setParameter("certificateId", certificateId);

        return Long.valueOf(query.getSingleResult().toString());
    }
}
