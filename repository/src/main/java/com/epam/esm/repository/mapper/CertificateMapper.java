package com.epam.esm.repository.mapper;

import com.epam.esm.model.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class CertificateMapper implements RowMapper<Certificate> {

    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getBigDecimal("price"));

        Timestamp creationTimestamp = resultSet.getTimestamp("creation");
        Instant creation = creationTimestamp.toLocalDateTime().toInstant(ZoneOffset.UTC);
        certificate.setCreation(creation);

        Timestamp modificationTimestamp = resultSet.getTimestamp("modification");
        if (modificationTimestamp != null) {
            Instant modification = modificationTimestamp.toLocalDateTime().toInstant(ZoneOffset.UTC); //utc->local
            certificate.setModification(modification);
        }
        certificate.setDuration(resultSet.getInt("duration"));

        return certificate;
    }
}
