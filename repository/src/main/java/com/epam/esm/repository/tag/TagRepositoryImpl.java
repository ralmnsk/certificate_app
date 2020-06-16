package com.epam.esm.repository.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository<Tag, Integer> {

    private static Logger logger = LoggerFactory.getLogger(TagRepositoryImpl.class);
    private final String SQL_FIND = "select tag.id, tag.name from tag where id = ?";
    private final String SQL_FIND_BY_NAME = "select tag.id, tag.name from tag where name = ?";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?) returning id,name";
    private final String SQL_GET_ALL = "select tag.id, tag.name from tag";
    private JdbcTemplate jdbcTemplate;
    private TagMapper tagMapper;

    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, new Object[]{name}, tagMapper));
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, tagMapper);
    }

    @Override
    public Optional<Tag> save(Tag tag) {
        return Optional.ofNullable(jdbcTemplate.query(SQL_INSERT, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, tag.getName());
            }
        }, tagMapper).get(0));
    }


    @Override
    public Optional<Tag> get(Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND, new Object[]{id}, tagMapper));
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        //It should not be implemented by the task
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }
}
