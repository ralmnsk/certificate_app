package com.epam.esm.service.validator;

import com.epam.esm.service.dto.FilterDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterValidatorTest {

    @Test
    void validateNumberFormatException() {
        FilterDto filterDto = new FilterDto();
        filterDto.setName("");
        filterDto.setPage("q");
        filterDto.setSize("qq");
        FilterDto validated = FilterValidator.validate(filterDto);
        assertEquals(validated.getPageInt(), 1);
        assertEquals(validated.getSizeInt(), 10);
    }

    @Test
    void parseSort() {
        FilterDto filterDto = new FilterDto();
        filterDto.setSort("sort=(name,creation)");
        FilterDto validated = FilterValidator.validate(filterDto);
        assertTrue(validated.getParams().contains("name"));
        assertTrue(validated.getParams().contains("creation"));
    }
}