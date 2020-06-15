package com.epam.esm.repository.certificate;

import com.epam.esm.model.Filter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateQueryBuilder {
    private final String SQL_START = "select distinct certificate.id,certificate.name,certificate.description,certificate.price," +
            " certificate.creation, certificate.modification, certificate.duration from certificate ";
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
    private final String SQL_EMPTY = "";

    private Filter filter;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String build(Filter filter){
        StringBuilder sql = new StringBuilder(SQL_START);

        boolean flag_tag_like = false;
        if (filter.getTagName() != null && !filter.getTagName().equals(SQL_EMPTY)) {
            sql.append(JOIN_TAG_NAME);
            flag_tag_like = true;
        }
        sql.append(WHERE);
        if (flag_tag_like) {
            sql.append(TAG_LIKE + AND);
        }
        sql.append(CERTIFICATE_LIKE);
//        if (filter.getName() == null) {
//            name = SQL_EMPTY;
//        }
        if (!filter.getParams().isEmpty()) {
            sql.append(ORDER_BY);
            boolean flag_comma =false;
            List<String> findName = filter
                    .getParams()
                    .stream()
                    .filter(p -> p.equals("name"))
                    .collect(Collectors.toList());
            if (!findName.isEmpty()) {
                sql.append(CERTIFICATE_NAME);
                flag_comma = true;
            }
            List<String> findDate = filter
                    .getParams()
                    .stream()
                    .filter(p->p.equals("creation"))
                    .collect(Collectors.toList());
            if (!findDate.isEmpty()) {
                if (flag_comma){
                    sql.append(COMMA).append(CERTIFICATE_CREATION);
                } else{
                    sql.append(CERTIFICATE_CREATION);
                }
            }
        }

        int page = filter.getPageInt();
        int size = filter.getSizeInt();
        sql.append(" LIMIT ").append(size);
        int offset = (page - 1)*size;
        sql.append(" OFFSET ").append(offset);

        return sql.toString();
    }
}
