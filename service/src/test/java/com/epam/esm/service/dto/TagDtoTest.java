package com.epam.esm.service.dto;

import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TagDtoTest {

    @Test
    void testEquals() {
        Tag tagA = new Tag();
        tagA.setName("testName");
        Tag tagB = new Tag();
        tagB.setName("testName");
        assertFalse(tagA.equals(tagB));
        tagB = tagA;
        assertEquals(tagA, tagB);
    }

    @Test
    void testHashCode() {
        Tag tagA = new Tag();
        tagA.setName("testName");
        Tag tagB = new Tag();
        tagB.setName("testName");
        assertEquals(tagA.hashCode(), tagB.hashCode());
    }
}