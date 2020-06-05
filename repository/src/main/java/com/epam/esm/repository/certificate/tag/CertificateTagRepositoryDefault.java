package com.epam.esm.repository.certificate.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class CertificateTagRepositoryDefault implements CertificateTagRepository {

    private static Logger logger = LoggerFactory.getLogger(CertificateTagRepositoryDefault.class);
    private final String SQL_CERTIFICATE_BY_TAG_ID = "select certificate_id from certificate_tag where tag_id = ?";
    private final String SQL_TAG_BY_CERTIFICATE_ID = "select tag_id from certificate_tag where certificate_id = ?";
    private final String SQL_DELETE = "delete from certificate_tag where certificate_id = ? and tag_id = ?";
    private final String SQL_INSERT = "insert into certificate_tag(certificate_id, tag_id) values(?,?)";
    private JdbcTemplate jdbcTemplate;

    public CertificateTagRepositoryDefault(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Long> getCertificateIdsByTagId(Long id) {
        return jdbcTemplate.queryForList(SQL_CERTIFICATE_BY_TAG_ID,new Object[]{id},Long.class);
    }

    @Override
    public List<Long> getTagIdsByCertificateId(Long id) {
        return jdbcTemplate.queryForList(SQL_TAG_BY_CERTIFICATE_ID, new Object[]{id},Long.class);
    }

    @Override
    public boolean saveCertificateTag(Long certId, Long tagId) {
        try {
            return jdbcTemplate.update(SQL_INSERT,
                    certId, tagId) > 0;
        } catch (DuplicateKeyException e) {
            logger.info("This certificate_tag already exists: certId={}, tagId={}", certId, tagId, e);
            return false;
        }
    }

    @Override
    public boolean deleteCertificateTag(Long certId, Long tagId) {
        return jdbcTemplate.update(SQL_DELETE, certId, tagId) > 0;
    }
}
