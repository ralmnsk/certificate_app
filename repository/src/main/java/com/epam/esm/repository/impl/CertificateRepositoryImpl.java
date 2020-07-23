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
    public final static String SELECT = "select";
    public final static String COUNT = "count";
    private QueryBuilder<CertificateFilter> builder;

    public CertificateRepositoryImpl(QueryBuilder<CertificateFilter> builder) {
        super(Certificate.class);
        this.builder = builder;
    }

    @Override
    public CertificateListWrapper getAll(CertificateFilter filter) {

        String ql = builder.assembleQlString(filter, Certificate.class, SELECT).toString();
        Query query = getEntityManager().createNativeQuery(ql, Certificate.class);
        builder.setParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Certificate> certificates = query.getResultList();

        Query queryTotal = getEntityManager().createNativeQuery
                (builder.assembleQlString(filter, Certificate.class, COUNT).toString());
        builder.setParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());

        filter = builder.updateFilter(filter, pageSize, countResult);

        CertificateListWrapper wrapper = new CertificateListWrapper();
        wrapper.setList(certificates);
        wrapper.setFilter(filter);

        return wrapper;
    }


}
