package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Transactional
public class TagRepositoryDefault implements TagRepository<Tag> {

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
    public Tag getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_NAME, new Object[]{name}, tagMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such tag name = {}", name, e);
            return null;
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, tagMapper);
    }

    @Override
    public boolean save(Tag tag) {
        if (tag != null && tag.getName() != null && getByName(tag.getName()) == null) {
            try {
                return jdbcTemplate.update(SQL_INSERT,
                        tag.getName()) > 0;
            } catch (DuplicateKeyException e) {
                logger.info("This tag already exists: {}", tag.getName(), e);
                return false;
            }
        }
        return false;
    }

    @Override
    public Tag get(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND, new Object[]{id}, tagMapper);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no tag id = {}", id, e);
            return null;
        }
    }

    @Override
    public boolean update(Tag tag) {
        return jdbcTemplate.update(SQL_UPDATE, tag.getName(),tag.getId()) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }
}
