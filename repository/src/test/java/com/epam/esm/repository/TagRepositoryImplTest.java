package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.repository.tag.TagRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryImplTest {
    private TagRepository<Tag, Integer> repository;
    private Tag one;
    private Tag two;

    @BeforeEach
    void setUp() {
        one = new Tag();
        one.setName("tagOne");
        two = new Tag();
        two.setName("tagTwo");

        ApplicationContext context = new AnnotationConfigApplicationContext(RepositoryConfiguration.class);
        repository = context.getBean(TagRepositoryImpl.class);
    }

    @AfterEach
    void tearDown() {
        Optional<Tag> byName = Optional.empty();
        try {
            byName = repository.getByName(one.getName());
            byName.ifPresent(tag -> repository.delete(tag.getId()));
        } catch (Exception ignored) {

        }
    }

    @Test
    void getByName() {
        repository.save(one);
        Optional<Tag> byName = repository.getByName(one.getName());
        assertEquals(byName.get().getName(), one.getName());
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
    void delete() {
        repository.save(one);
        Optional<Tag> tag = repository.getByName(one.getName());
        Integer id = tag.get().getId();
        assertTrue(repository.delete(id));
    }

    @Test
    void getByLoginNoInDB() {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> repository.getByName("no"));
    }

    @Test
    void getByIdNoInDB() {
        Exception exception = assertThrows(EmptyResultDataAccessException.class, () -> repository.get(111111111));
    }

    @Test
    void deleteNoInDB() {
        boolean deleted = repository.delete(1111133333);
        assertFalse(deleted);
    }
}