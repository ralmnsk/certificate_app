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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryDefaultTest {
    private TagRepository<Tag,Long> repository;
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
        Optional<Tag> byName = repository.getByName(one.getName());
        if (byName.isPresent()) {
            repository.delete(byName.get().getId());
        }
    }

    @Test
    void getByName() {
        repository.save(one);
        Optional<Tag> byName = repository.getByName(one.getName());
        assertTrue(byName.get().getName().equals(one.getName()));
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
        repository.delete(repository.getByName(two.getName()).get().getId());
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
        assertTrue(repository.save(one).isPresent());
    }

    @Test
    void get() {
        repository.save(one);
        Optional<Tag> byName = repository.getByName(one.getName());
        assertEquals(byName.get().getName(), one.getName());
    }

    @Test
    void update() {
        repository.save(one);
        Optional<Tag> read = repository.getByName(one.getName());
        read.get().setName("new_name");
        repository.update(read.get());
        Optional<Tag> updated = repository.get(read.get().getId());
        assertEquals("new_name", updated.get().getName());
        one = updated.get();
        assertFalse(repository.update(two).isPresent());
    }

    @Test
    void delete() {
        repository.save(one);
        Optional<Tag> tag = repository.getByName(one.getName());
        Long id = tag.get().getId();
        repository.delete(id);
        Optional<Tag> check = repository.get(id);
        assertFalse(check.isPresent());
    }

    @Test
    void getByLoginNoInDB(){
        Optional<Tag> tag = repository.getByName("no");
        assertFalse(tag.isPresent());
    }

    @Test
    void getByIdNoInDB(){
        Optional<Tag> tag = repository.get(111111111L);
        assertFalse(tag.isPresent());
    }

    @Test
    void saveAlreadyExists() throws Exception{
        repository.save(one);
        Optional<Tag> saved = repository.save(one);
        assertFalse(saved.isPresent());
    }

    @Test
    void deleteNoInDB(){
        boolean deleted = repository.delete(1111133333L);
        assertFalse(deleted);
    }
}