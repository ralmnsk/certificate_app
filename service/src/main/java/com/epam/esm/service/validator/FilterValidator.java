package com.epam.esm.service.validator;

import com.epam.esm.service.dto.FilterDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class FilterValidator {
    private static Logger logger = LoggerFactory.getLogger(FilterValidator.class);

    public static FilterDto validate(FilterDto filterDto) {
        if (filterDto != null) {
            String tagName = "";
            String name = "";
            int pageInt = 1;
            int sizeInt = 10;

            if (filterDto.getTagName() != null) {
                tagName = filterDto.getTagName();
            }
            if (filterDto.getName() != null) {
                name = filterDto.getName();
            }
            if (filterDto.getSize() != null) {
                try {
                    sizeInt = Integer.parseInt(filterDto.getSize());
                } catch (NumberFormatException e) {
                    logger.info(FilterValidator.class + " set page size to default =10 " + e);
                    sizeInt = 10;
                }
            }
            if (filterDto.getPage() != null) {
                try {
                    pageInt = Integer.parseInt(filterDto.getPage());
                } catch (NumberFormatException e) {
                    logger.info(FilterValidator.class + " set page to default =1 " + e);
                    pageInt = 1;
                }
            }
            if (filterDto.getSort() != null) {
                parseSort(filterDto);
            }
            filterDto.setTagName(tagName);
            filterDto.setName(name);
            filterDto.setPageInt(pageInt);
            filterDto.setSizeInt(sizeInt);

        }
        return filterDto;
    }


    private static List<String> parseSort(FilterDto filterDto) {
        String sort = filterDto.getSort();
        sort = sort.replace("sort=(", "");
        sort = sort.replace(")", "");
        String[] split = sort.split(",");
        Arrays.stream(split).filter(Objects::nonNull)
                .map(String::trim).forEach(s -> filterDto.getParams().add(s));
        return filterDto.getParams();
    }
}
