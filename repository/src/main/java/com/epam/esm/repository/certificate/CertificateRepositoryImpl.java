package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateRepositoryImpl implements CertificateRepository<Certificate, Long> {

    private static Logger logger = LoggerFactory.getLogger(CertificateRepositoryImpl.class);
    private final String SQL_FIND_CERTIFICATE = "select certificate.id,certificate.name,certificate.description,certificate.price, certificate.creation, certificate.modification, certificate.duration from certificate where id = ?";
    private final String SQL_FIND_CERTIFICATE_BY_NAME = "select certificate.id,certificate.name,certificate.description,certificate.price, certificate.creation, certificate.modification, certificate.duration from certificate where name = ?";
    private final String SQL_DELETE_CERTIFICATE = "delete from certificate where id = ?";
    private final String SQL_UPDATE_CERTIFICATE = "update certificate set name = ?, description = ?, price  = ?, duration = ? where id = ?";

    private final String SQL_INSERT_CERTIFICATE = "insert into certificate(name, description, price, duration) values(?,?,?,?) returning id,name,description,price,creation,modification,duration";
    private final String SQL_PERCENT = "%";
    private final String SQL_EMPTY = "";

    private JdbcTemplate jdbcTemplate;
    private CertificateMapper certificateMapper;
    private CertificateQueryBuilder queryBuilder;

    public CertificateRepositoryImpl(JdbcTemplate jdbcTemplate,
                                     CertificateMapper certificateMapper,
                                     CertificateQueryBuilder queryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateMapper = certificateMapper;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<Certificate> getByName(String name) {
        return jdbcTemplate.query(SQL_FIND_CERTIFICATE_BY_NAME, new Object[]{name}, certificateMapper);
    }

    @Override
    public List<Certificate> getAll(Filter filter) {
        String sql = queryBuilder.build(filter);
        logger.info("SQL query for {} {}", this.getClass(), sql);

        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (filter.getTagName() != null && !filter.getTagName().equals(SQL_EMPTY)) {
                    ps.setString(1, SQL_PERCENT + filter.getTagName() + SQL_PERCENT);
                    ps.setString(2, SQL_PERCENT + filter.getName() + SQL_PERCENT);
                } else {
                    ps.setString(1, SQL_PERCENT + filter.getName() + SQL_PERCENT);
                }
            }
        }, certificateMapper);
    }

    @Override
    public Optional<Certificate> save(Certificate certificate) {
        return Optional.ofNullable(jdbcTemplate.query(SQL_INSERT_CERTIFICATE, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, certificate.getName());
                ps.setString(2, certificate.getDescription());
                ps.setBigDecimal(3, certificate.getPrice());
                ps.setInt(4, certificate.getDuration());
            }
        }, certificateMapper).get(0));
    }

    @Override
    public Optional<Certificate> get(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id}, certificateMapper));
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getId());

        return get(certificate.getId());
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) > 0;
    }
}
