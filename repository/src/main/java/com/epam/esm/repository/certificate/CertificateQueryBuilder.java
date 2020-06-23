package com.epam.esm.repository.certificate;

import com.epam.esm.model.Filter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Certificate query builder.
 * The CertificateQueryBuilder creates a query for getting certificates
 * from a database and a query for counting filtered certificates.
 */
@Component
public class CertificateQueryBuilder {
    private final String SQL_START = "select distinct certificate.id,certificate.name,certificate.description,certificate.price," +
            " certificate.creation, certificate.modification, certificate.duration from certificate ";
    private final String SQL_START_COUNT = "select distinct count (*) from certificate ";
    private final String JOIN_TAG_NAME = " join certificate_tag on certificate_tag.certificate_id=certificate.id " +
            " join tag on certificate_tag.tag_id = tag.id ";
    private final String WHERE = " where ";
    private final String TAG_LIKE = " tag.name like ? ";
    private final String AND = " and ";
    private final String CERTIFICATE_LIKE = " certificate.name like ? ";
    private final String ORDER_BY = " order by ";
    private final String CERTIFICATE_NAME = " certificate.name desc ";
    private final String COMMA = " , ";
    private final String CERTIFICATE_CREATION = " certificate.creation desc ";
    private final int FIRST_PAGE_NUMBER = 1;

    private Filter filter;

    /**
     * Gets filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets filter.
     *
     * @param filter the filter for the creating query
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Build the string sql query.
     *
     * @param filter the filter
     * @return the string of query
     */
    public String build(Filter filter) {
        StringBuilder sql = new StringBuilder(SQL_START);
        if (filter.isTurnCountingOn()) {
            sql = new StringBuilder(SQL_START_COUNT);
        }

        boolean flagForTagLike = false;
        if (filter.getTagName() != null && !filter.getTagName().isEmpty()) {
            sql.append(JOIN_TAG_NAME);
            flagForTagLike = true;
        }
        sql.append(WHERE);
        if (flagForTagLike) {
            sql.append(TAG_LIKE + AND);
        }
        sql.append(CERTIFICATE_LIKE);
        if (filter.getSortParams() != null
                && (filter.getSortParams().contains("name")
                || filter.getSortParams().contains("creation"))
                && !filter.isTurnCountingOn()) {
            sql.append(ORDER_BY);
            boolean flag_comma = false;
            List<String> findName = filter
                    .getSortParams()
                    .stream()
                    .filter(p -> p.equals("name"))
                    .collect(Collectors.toList());
            if (!findName.isEmpty()) {
                sql.append(CERTIFICATE_NAME);
                flag_comma = true;
            }
            List<String> findDate = filter
                    .getSortParams()
                    .stream()
                    .filter(p -> p.equals("creation"))
                    .collect(Collectors.toList());
            if (!findDate.isEmpty()) {
                if (flag_comma) {
                    sql.append(COMMA).append(CERTIFICATE_CREATION);
                } else {
                    sql.append(CERTIFICATE_CREATION);
                }
            }
        }

        int page = filter.getPage();
        int size = filter.getSize();
        if (!filter.isTurnCountingOn()) {
            sql.append(" LIMIT ").append(size);
            int offset = (page - FIRST_PAGE_NUMBER) * size;
            sql.append(" OFFSET ").append(offset);
        }

        return sql.toString();
    }
}
