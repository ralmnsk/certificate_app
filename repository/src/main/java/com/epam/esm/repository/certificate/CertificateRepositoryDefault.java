package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public class CertificateRepositoryDefault implements CertificateRepository<Certificate> {

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
    public Certificate getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE_BY_NAME, new Object[]{name}, certificateMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such certificate name = {}", name, e);
            return null;
        }
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, certificateMapper);
    }

    @Override
    public boolean save(Certificate certificate) {
        if (certificate != null && certificate.getName() != null && getByName(certificate.getName()) == null) {
            try {
                return jdbcTemplate.update(SQL_INSERT_CERTIFICATE,
                        certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                        Timestamp.from(certificate.getCreation()), Timestamp.from(certificate.getModification()), certificate.getDuration()) > 0;
            } catch (DuplicateKeyException e) {
                logger.info("This certificate already exists: {}", certificate.getName(), e);

            }
        }

        return false;
    }

    @Override
    public Certificate get(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id}, certificateMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate id = {}", id, e);
            return null;
        }
    }

    @Override
    public boolean update(Certificate certificate) {
        return jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(),
                Timestamp.from(certificate.getCreation()), Timestamp.from(certificate.getModification()),
                certificate.getDuration(), certificate.getId()) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id) > 0;
    }
}
