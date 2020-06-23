package com.epam.esm.repository.mapper;

import com.epam.esm.model.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;

import static com.epam.esm.repository.RepositoryConstants.*;

/**
 * The type Certificate mapper implements {@link org.springframework.jdbc.core.RowMapper} interface.
 * The CertificateMapper converts a ResultSet from a database into Certificate.
 */
@Component
public class CertificateMapper implements RowMapper<Certificate> {

    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong(ID));
        certificate.setName(resultSet.getString(NAME));
        certificate.setDescription(resultSet.getString(DESCRIPTION));
        certificate.setPrice(resultSet.getBigDecimal(PRICE));

        Timestamp creationTimestamp = resultSet.getTimestamp(CREATION);
        Instant creation = creationTimestamp.toLocalDateTime().toInstant(ZoneOffset.UTC);
        certificate.setCreation(creation);

        Timestamp modificationTimestamp = resultSet.getTimestamp(MODIFICATION);
        if (modificationTimestamp != null) {
            Instant modification = modificationTimestamp.toLocalDateTime().toInstant(ZoneOffset.UTC);
            certificate.setModification(modification);
        }
        certificate.setDuration(resultSet.getInt(DURATION));

        return certificate;
    }
}
