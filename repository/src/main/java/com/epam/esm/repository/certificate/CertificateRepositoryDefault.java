package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.exception.UpdateCertificateException;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateRepositoryDefault implements CertificateRepository<Certificate,Long> {

    private static Logger logger = LoggerFactory.getLogger(CertificateRepositoryDefault.class);
    private final String SQL_FIND_CERTIFICATE = "select * from certificate where id = ?";
    private final String SQL_FIND_CERTIFICATE_BY_NAME = "select * from certificate where name = ?";
    private final String SQL_DELETE_CERTIFICATE = "delete from certificate where id = ?";
    private final String SQL_UPDATE_CERTIFICATE = "update certificate set name = ?, description = ?, price  = ?, creation = ?, modification = ?, duration = ? where id = ?";
    private final String SQL_GET_ALL = "select * from certificate";
    private final String SQL_INSERT_CERTIFICATE = "insert into certificate(name, description, price, creation, modification, duration) values(?,?,?,?,?,?)";
    private JdbcTemplate jdbcTemplate;
    private CertificateMapper certificateMapper;

    @Autowired
    public CertificateRepositoryDefault(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        certificateMapper = new CertificateMapper();
    }

    @Override
    public Optional<Certificate> getByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_BY_NAME, new Object[]{name}, certificateMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such certificate name = {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, certificateMapper);
    }

    @Override
    public Optional<Certificate> save(Certificate certificate) {
        if (certificate != null && certificate.getName() != null && !getByName(certificate.getName()).isPresent()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            try {
                if(jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, certificate.getName());
                    ps.setString(2, certificate.getDescription());
                    ps.setBigDecimal(3, certificate.getPrice());
                    ps.setTimestamp(4, Timestamp.from(certificate.getCreation()));
                    ps.setTimestamp(5, Timestamp.from(certificate.getModification()));
                    ps.setInt(6,certificate.getDuration());
                    return ps;
                },keyHolder) > 0){
                    Long id = Long.valueOf((Integer) keyHolder.getKeys().get("id"));
                    certificate.setId(id);
                    return Optional.ofNullable(certificate);
                }
            } catch (DuplicateKeyException e) {
                logger.info("This certificate already exists: {}", certificate.getName(), e);
            } catch(NullPointerException e){
                logger. info("Certificate id Nullpointer exception", e);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Certificate> get(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id}, certificateMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate id = {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        if (certificate != null && certificate.getName() != null && certificate.getId() != null) {
            jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                    certificate.getDescription(), certificate.getPrice(),
                    Timestamp.from(certificate.getCreation()), Timestamp.from(certificate.getModification()),
                    certificate.getDuration(), certificate.getId());
        } else {
            try {
                throw new UpdateCertificateException("certificate and certificate.name have to be not null and id > 0");
            } catch (UpdateCertificateException e) {
                logger.info(e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.ofNullable(certificate);
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) > 0;
    }
}
