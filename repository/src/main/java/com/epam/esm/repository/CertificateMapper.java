package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class CertificateMapper implements RowMapper<Certificate> {
    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getBigDecimal("price"));

        Timestamp creationTimestamp = resultSet.getTimestamp("creation");
        Instant creation = creationTimestamp.toInstant();
        certificate.setCreation(creation);

        Timestamp modificationTimestamp = resultSet.getTimestamp("modification");
        Instant modification = modificationTimestamp.toInstant();
        certificate.setModification(modification);
        certificate.setDuration(resultSet.getInt("duration"));

        return certificate;
    }
}
