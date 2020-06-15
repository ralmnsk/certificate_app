package com.epam.esm.repository.mapper;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.repository.RepositoryConstants.ID;
import static com.epam.esm.repository.RepositoryConstants.NAME;

@Component
public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt(ID));
        tag.setName(rs.getString(NAME));

        return tag;
    }
}
