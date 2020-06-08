package com.epam.esm.repository.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.exception.UpdateTagException;
import com.epam.esm.repository.mapper.TagMapper;
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
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagRepositoryDefault implements TagRepository<Tag, Long> {

    private static Logger logger = LoggerFactory.getLogger(TagRepositoryDefault.class);
    private final String SQL_FIND = "select * from tag where id = ?";
    private final String SQL_FIND_BY_NAME = "select * from tag where name = ?";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?)";
    private final String SQL_UPDATE = "update tag set name = ? where id = ?";
    private final String SQL_GET_ALL = "select * from tag";
    private JdbcTemplate jdbcTemplate;
    private TagMapper tagMapper;

    @Autowired
    public TagRepositoryDefault(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        tagMapper = new TagMapper();
    }

    @Override
    public Optional<Tag> getByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, new Object[]{name}, tagMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such tag name = {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, tagMapper);
    }

    @Override
    public Optional<Tag> save(Tag tag) {

        if (tag != null && tag.getName() != null && !getByName(tag.getName()).isPresent()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            try {
                if (jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, tag.getName());
                    return ps;
                }, keyHolder) > 0) {
                    Long id = Long.valueOf((Integer) keyHolder.getKeys().get("id"));
                    tag.setId(id);
                    return Optional.ofNullable(tag);
                }
            } catch (DuplicateKeyException e) {
                logger.info("This tag already exists: {} {}", tag.getName(), e);
            } catch (NullPointerException e) {
                logger.info("Tag id Nullpointer exception ", e);
            }
        }
        return Optional.empty();
    }


    @Override
    public Optional<Tag> get(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_FIND, new Object[]{id}, tagMapper));
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no tag id = {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        if (tag != null && tag.getName() != null && tag.getId() != null) {
            jdbcTemplate.update(SQL_UPDATE, tag.getName(), tag.getId());
        } else {
            try {
                throw new UpdateTagException("tag and tag.name have to be not null and id > 0");
            } catch (UpdateTagException e) {
                logger.info(e.getMessage());
                return Optional.empty();
            }
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }
}
