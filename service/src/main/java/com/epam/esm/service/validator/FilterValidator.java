package com.epam.esm.service.validator;

import com.epam.esm.repository.certificate.FilterDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class FilterValidator {
    private FilterDto filter;
    private static Logger logger = LoggerFactory.getLogger(FilterValidator.class);

    public FilterDto getFilter() {
        return filter;
    }

    public void setFilter(FilterDto filter) {
        this.filter = filter;
    }

    public FilterDto validate(FilterDto filter){
        this.filter = filter;
        if (filter != null){
            String tagName = "";
            String name = "";
            int pageInt = 1;
            int sizeInt = 10;

            if (filter.getTagName() != null){
                tagName = filter.getTagName();
            }
            if (filter.getName() != null){
                name = filter.getName();
            }
            if(filter.getSize() != null){
                try {
                    sizeInt = Integer.parseInt(filter.getSize());
                } catch(NumberFormatException e){
                    logger.info(this.getClass()+ " set page size to default =10 "+e);
                    sizeInt = 10;
                }
            }
            if(filter.getPage() != null){
                try {
                    pageInt = Integer.parseInt(filter.getPage());
                } catch(NumberFormatException e){
                    logger.info(this.getClass()+ " set page to default =1 "+e);
                    pageInt = 1;
                }
            }
            if (filter.getSort() != null){
                parseSort(filter.getSort());
            }
            filter.setTagName(tagName);
            filter.setName(name);
            filter.setPageInt(pageInt);
            filter.setSizeInt(sizeInt);

        }
        return filter;
    }


    private List<String> parseSort(String sort){
        sort = sort.replace("(","");
        sort = sort.replace(")","");
        String[] split = sort.split(",");
        Arrays.stream(split).filter(s->s != null)
                .map(String::trim).forEach(s->filter.getParams().add(s));
        return filter.getParams();
    }
}
