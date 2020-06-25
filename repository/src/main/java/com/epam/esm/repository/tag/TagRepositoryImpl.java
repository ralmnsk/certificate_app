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

/**
 * The type Tag repository implementation.
 * The TagRepositoryImpl uses crud operations with help of
 * {@link org.springframework.jdbc.core.JdbcTemplate} to interact with
 * a database, and TagMapper {@link com.epam.esm.repository.mapper.TagMapper}
 * converts ResultSet into Certificate.
 */
@Repository
public class TagRepositoryImpl implements TagRepository<Tag, Integer> {

    private static Logger logger = LoggerFactory.getLogger(TagRepositoryImpl.class);
    private final String SQL_FIND = "select tag.id, tag.name from tag where id = ?";
    private final String SQL_FIND_BY_NAME = "select tag.id, tag.name from tag where name = ?";
    private final String SQL_DELETE = "delete from tag where id = ?";
    private final String SQL_INSERT = "insert into tag(name) values(?) returning id,name";
    private final String SQL_GET_ALL = "select tag.id, tag.name from tag";

    private final String SQL_DELETE_MIDDLE_TAG = "delete from certificate_tag where tag_id = ? ";
    private final String SQL_TAGS_BY_CERTIFICATE_ID = " select tag.id, tag.name from certificate_tag join tag on tag_id=tag.id where certificate_tag.certificate_id = ? ";
    private final String SQL_REMOVE_TAGS_BY_CERT_ID = "delete from certificate_tag where certificate_id = ? ";

    private JdbcTemplate jdbcTemplate;
    private TagMapper tagMapper;

    /**
     * Instantiates a new Tag repository.
     * Spring injects parameters automatically.
     *
     * @param jdbcTemplate the jdbc template
     * @param tagMapper    the tag mapper
     */
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
        jdbcTemplate.update(SQL_DELETE_MIDDLE_TAG, id);
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

    @Override
    public List<Tag> getTagsByCertificateId(Long id) {
        return jdbcTemplate.query(SQL_TAGS_BY_CERTIFICATE_ID, new Object[]{id}, tagMapper);
    }

    @Override
    public void removeTagsByCertificateId(Long id) {
        jdbcTemplate.update(SQL_REMOVE_TAGS_BY_CERT_ID, id);
    }
}
