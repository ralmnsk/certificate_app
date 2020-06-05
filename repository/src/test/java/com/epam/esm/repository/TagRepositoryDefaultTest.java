package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.tag.TagRepositoryDefault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryDefaultTest {
    private TagRepository<Tag> repository;
    private Tag one;
    private Tag two;

    @BeforeEach
    void setUp() {
        one = new Tag();
        one.setName("tagOne");
        two = new Tag();
        two.setName("tagTwo");

        ApplicationContext context = new AnnotationConfigApplicationContext(SpringJdbcConfig.class);
        repository = context.getBean(TagRepositoryDefault.class);
    }

    @AfterEach
    void tearDown() {
        Tag byName = repository.getByName(one.getName());
        if (byName != null) {
            repository.delete(byName.getId());
        }
    }

    @Test
    void getByName() {
        repository.save(one);
        Tag byName = repository.getByName(one.getName());
        assertTrue(byName.getName().equals(one.getName()));
    }

    @Test
    void getAll() {
        repository.save(one);
        repository.save(two);
        List<Tag> tags = repository.getAll();
        assertTrue(tags.size() > 0);
        assertTrue(tags
                .stream()
                .filter(u -> u.getName().equals(one.getName()))
                .collect(Collectors.toList()).size() > 0);
        assertTrue(tags
                .stream()
                .filter(u -> u.getName().equals(two.getName()))
                .collect(Collectors.toList()).size() > 0);
        repository.delete(repository.getByName(two.getName()).getId());
    }

    @Test
    void getAllNoInDB() throws Exception{
        List<Tag> tags = repository.getAll();
        assertTrue(tags
                .stream()
                .filter(u -> u.getName().equals(one.getName()))
                .collect(Collectors.toList()).size() == 0);
    }

    @Test
    void save() {
        assertTrue(repository.save(one));
    }

    @Test
    void get() {
        repository.save(one);
        Tag byName = repository.getByName(one.getName());
        assertEquals(byName.getName(), one.getName());
    }

    @Test
    void update() {
        repository.save(one);
        Tag read = repository.getByName(one.getName());
        read.setName("new_name");
        repository.update(read);
        Tag updated = repository.get(read.getId());
        assertEquals("new_name", updated.getName());
        one = updated;
        assertFalse(repository.update(two));
    }

    @Test
    void delete() {
        repository.save(one);
        Tag tag = repository.getByName(one.getName());
        Long id = tag.getId();
        repository.delete(id);
        Tag check = repository.get(id);
        assertNull(check);
    }

    @Test
    void getByLoginNoInDB(){
        Tag tag = repository.getByName("no");
        assertNull(tag);
    }

    @Test
    void getByIdNoInDB(){
        Tag tag = repository.get(111111111L);
        assertNull(tag);
    }

    @Test
    void saveAlreadyExists() throws Exception{
        repository.save(one);
        boolean saved = repository.save(one);
        assertFalse(saved);
    }

    @Test
    void deleteNoInDB(){
        boolean deleted = repository.delete(1111133333L);
        assertFalse(deleted);
    }
}