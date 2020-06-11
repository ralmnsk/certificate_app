package com.epam.esm.repository.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository<Tag, Long> {

    private static Logger logger = LoggerFactory.getLogger(TagRepositoryImpl.class);
    private final String SQL_FIND = "select tag.id, tag.name from tag where id = ?";
    private final String SQL_FIND_BY_NAME = "select tag.id, tag.name from tag where name = ?";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?)";
    private final String SQL_UPDATE = "update tag set name = ? where id = ?";
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        Long id = Long.valueOf((Integer) keyHolder.getKeys().get("id"));
        tag.setId(id);

        return Optional.ofNullable(tag);

    }


    @Override
    public Optional<Tag> get(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND, new Object[]{id}, tagMapper));
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }
}
