package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.exception.UpdateCertificateException;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateRepositoryImpl implements CertificateRepository<Certificate, Long> {

    private static Logger logger = LoggerFactory.getLogger(CertificateRepositoryImpl.class);
    private final String SQL_FIND_CERTIFICATE = "select * from certificate where id = ?";
    private final String SQL_FIND_CERTIFICATE_BY_NAME = "select * from certificate where name = ?";
    private final String SQL_DELETE_CERTIFICATE = "delete from certificate where id = ?";
    private final String SQL_UPDATE_CERTIFICATE = "update certificate set name = ?, description = ?, price  = ?, creation = ?, modification = ?, duration = ? where id = ?";

    private final String SQL_INSERT_CERTIFICATE = "insert into certificate(name, description, price, creation, modification, duration) values(?,?,?,?,?,?)";
    private final String SQL_PERCENT = "%";
    private final String SQL_EMPTY = "";

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
    private final String CERTIFICATE_CREATION = "certificate.creation desc";

    private JdbcTemplate jdbcTemplate;
    private CertificateMapper certificateMapper;


    public CertificateRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        certificateMapper = new CertificateMapper();
    }

    @Override
    public Optional<Certificate> getByName(String name) throws EmptyResultDataAccessException {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_BY_NAME, new Object[]{name}, certificateMapper));
    }

    @Override
    public List<Certificate> getAll(String tagName, String name, String sortByName, String sortByDate) {
        String sql = SQL_START;
        boolean flag_tag_like = false;
        if (tagName != null && !tagName.equals(SQL_EMPTY)) {
            sql = sql + JOIN_TAG_NAME;
            flag_tag_like = true;
        }
        sql = sql + WHERE;
        if (flag_tag_like) {
            sql = sql + TAG_LIKE + AND;
        }
        sql =sql + CERTIFICATE_LIKE;
        if (name == null) {
            name = SQL_EMPTY;
        }
        if ((sortByName != null) || (sortByDate != null)) {
            sql = sql + ORDER_BY;
            boolean flag_comma =false;
            if (sortByName != null) {
                sql = sql + CERTIFICATE_NAME;
                flag_comma = true;
            }
            if (sortByDate != null) {
                if (flag_comma){
                    sql = sql + COMMA + CERTIFICATE_CREATION;
                } else{
                    sql = sql + CERTIFICATE_CREATION;
                }
            }
        }

        logger.info("SQL query for {} {}", this.getClass(), sql);

        String finalTagName = tagName;
        String finalName = name;

        return jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                if (tagName != null && !tagName.equals(SQL_EMPTY)){
                    ps.setString(1, SQL_PERCENT + finalTagName + SQL_PERCENT);
                    ps.setString(2, SQL_PERCENT + finalName + SQL_PERCENT);
                } else {
                    ps.setString(1, SQL_PERCENT + finalName + SQL_PERCENT);
                }
            }
        }, certificateMapper);
    }

    @Override
    public Optional<Certificate> save(Certificate certificate) throws DuplicateKeyException, NullPointerException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setTimestamp(4, Timestamp.from(certificate.getCreation()));
            ps.setTimestamp(5, Timestamp.from(certificate.getModification()));
            ps.setInt(6, certificate.getDuration());
            return ps;
        }, keyHolder);
        Long id = Long.valueOf((Integer) keyHolder.getKeys().get("id"));
        certificate.setId(id);

        return Optional.of(certificate);
    }

    @Override
    public Optional<Certificate> get(Long id) throws EmptyResultDataAccessException {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id}, certificateMapper));
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) throws UpdateCertificateException {
        jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(),
                Timestamp.from(certificate.getCreation()), Timestamp.from(certificate.getModification()),
                certificate.getDuration(), certificate.getId());

        return Optional.ofNullable(certificate);
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) > 0;
    }
}
