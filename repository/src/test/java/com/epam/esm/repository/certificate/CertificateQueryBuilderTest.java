package com.epam.esm.repository.certificate;

import com.epam.esm.model.Filter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateQueryBuilderTest {

    @Test
    void build() {
        String sql = "select distinct certificate.id,certificate.name,certificate.description,certificate.price, certificate.creation, certificate.modification, certificate.duration from certificate  where  certificate.name like ?  order by  certificate.name desc  ,  certificate.creation desc  LIMIT 12 OFFSET 0";
        Filter filter = new Filter();
        filter.setName("cert");
        filter.setPage(1);
        filter.setSize(12);
        filter.setSortParams(Arrays.asList("name", "creation"));
        CertificateQueryBuilder builder = new CertificateQueryBuilder();
        String build = builder.build(filter);
        assertEquals(sql, build);
    }
}