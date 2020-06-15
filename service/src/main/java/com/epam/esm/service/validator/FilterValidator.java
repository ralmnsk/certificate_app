package com.epam.esm.service.validator;

import com.epam.esm.model.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class FilterValidator {
    private static Logger logger = LoggerFactory.getLogger(FilterValidator.class);

    public static Filter validate(Filter filter) {
        if (filter != null) {
            String tagName = "";
            String name = "";
            int pageInt = 1;
            int sizeInt = 10;

            if (filter.getTagName() != null) {
                tagName = filter.getTagName();
            }
            if (filter.getName() != null) {
                name = filter.getName();
            }
            if (filter.getSize() != null) {
                try {
                    sizeInt = Integer.parseInt(filter.getSize());
                } catch (NumberFormatException e) {
                    logger.info(FilterValidator.class + " set page size to default =10 " + e);
                    sizeInt = 10;
                }
            }
            if (filter.getPage() != null) {
                try {
                    pageInt = Integer.parseInt(filter.getPage());
                } catch (NumberFormatException e) {
                    logger.info(FilterValidator.class + " set page to default =1 " + e);
                    pageInt = 1;
                }
            }
            if (filter.getSort() != null) {
                parseSort(filter);
            }
            filter.setTagName(tagName);
            filter.setName(name);
            filter.setPageInt(pageInt);
            filter.setSizeInt(sizeInt);

        }
        return filter;
    }


    private static List<String> parseSort(Filter filter) {
        String sort = filter.getSort();
        sort = sort.replace("(", "");
        sort = sort.replace(")", "");
        String[] split = sort.split(",");
        Arrays.stream(split).filter(Objects::nonNull)
                .map(String::trim).forEach(s -> filter.getParams().add(s));
        return filter.getParams();
    }
}
