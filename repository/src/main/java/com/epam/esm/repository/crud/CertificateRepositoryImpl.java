package com.epam.esm.repository.crud;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.wrapper.CertificateListWrapper;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static com.epam.esm.repository.crud.Constants.COUNT;
import static com.epam.esm.repository.crud.Constants.SELECT;

@Repository
public class CertificateRepositoryImpl extends AbstractRepository<Certificate, Long> implements CertificateRepository {
    private final String EMPTY = "";
    private QueryBuilder<CertificateFilter> builder;

    public CertificateRepositoryImpl(QueryBuilder<CertificateFilter> builder) {
        super(Certificate.class);
        this.builder = builder;
    }

    @Override
    public CertificateListWrapper getAll(CertificateFilter filter) {

        String ql = builder.assembleQlString(filter, Certificate.class, SELECT);
        Query query = getEntityManager().createNativeQuery(ql, Certificate.class);
        builder.setParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Certificate> certificates = query.getResultList();

        Query queryTotal = getEntityManager().createNativeQuery
                (builder.assembleQlString(filter, Certificate.class, COUNT));
        builder.setParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());

        filter = builder.updateFilter(filter, pageSize, countResult);

        CertificateListWrapper wrapper = new CertificateListWrapper();
        wrapper.setList(certificates);
        wrapper.setFilter(filter);

        return wrapper;
    }


}
