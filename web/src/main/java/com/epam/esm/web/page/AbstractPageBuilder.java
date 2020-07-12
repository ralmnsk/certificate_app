package com.epam.esm.web.page;

import com.epam.esm.page.Direction;
import com.epam.esm.page.Order;
import com.epam.esm.page.Sort;
import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.web.assembler.Assembler;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AbstractPageBuilder<T, S extends CrudService, A extends Assembler> {
    private final String EMPTY = "";
    private Set<String> fieldSet;
    private S service;
    private A assembler;

    public AbstractPageBuilder(Set<String> fieldSet, S service, A assembler) {
        this.fieldSet = fieldSet;
        this.service = service;
        this.assembler = assembler;
    }

    public CustomPageDto<T> build(FilterDto filterDto) {
        CustomPageDto<T> page = new CustomPageDto<>();
        filterDto = validateFilter(filterDto);

        filterDto = setSort(filterDto);
        service.getAll(filterDto);
        CollectionModel<T> collectionModel = getCollectionModel(filterDto);

        filterDto = service.getFilterDto();
        page.setSize(filterDto.getSize());
        page.setTotalElements(filterDto.getTotalElements());
        page.setPage(filterDto.getPage());
        page.setElements(collectionModel);
        page.setTotalPage(filterDto.getTotalPages());

        return page;
    }

    private CollectionModel<T> getCollectionModel(FilterDto filterDto) {
        return assembler.toCollectionModel(filterDto);
    }

    public FilterDto setSort(FilterDto filterDto) {
        List<String> paramsNonFiltered = filterDto.getSortParams();
        List<String> params = paramsNonFiltered.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Sort sort = getSort(params);
        filterDto.setSort(sort);
        return filterDto;
    }

    public FilterDto validateFilter(FilterDto filter) {
        if (filter.getTagName() == null) {
            filter.setTagName(EMPTY);
        }
        if (filter.getCertificateName() == null) {
            filter.setCertificateName(EMPTY);
        }
        if (filter.getUserSurname() == null) {
            filter.setUserSurname(EMPTY);
        }
        if (filter.getCreation() == null) {
            filter.setCreation("1970-01-01 00:00:00");
        }
        if (filter.getModification() == null) {
            filter.setModification("1970-01-01 00:00:00");
        }
        if (filter.getDescription() == null) {
            filter.setDescription(EMPTY);
        }
        if (filter.getPrice() == null) {
            filter.setPrice(new BigDecimal(0.00));
        }
        if (filter.getDuration() == null) {
            filter.setDuration(0);
        }
        if (filter.getUserSurname() == null) {
            filter.setUserSurname(EMPTY);
        }
        if (filter.getUserName() == null) {
            filter.setUserName(EMPTY);
        }
        if (filter.getSize() == 0) {
            filter.setSize(5);
        }
        if (filter.getPage() == 0) {
            filter.setPage(0);
        }
        if (filter.getSortParams() == null) {
            List<String> params = new ArrayList<>();
            params.add("name+");
            filter.setSortParams(params);
        }

        return filter;
    }


    public Sort getSort(List<String> params) {
        List<Order> orders = new ArrayList<>();
        for (String param : params) {
            if (param.matches("[a-z.]{0,20}(([+]{0,1})|([-]{0,1}))")) {
                com.epam.esm.page.Direction direction = com.epam.esm.page.Direction.DESC;
                param = param.trim();
                if (param.contains("+")) {
                    direction = Direction.ASC;
                    param = param.replace("+", "");
                } else {
                    param = param.replace("-", "");
                }
                if (fieldSet.contains(param)) {
                    Order order = new Order(direction, param);
                    orders.add(order);
                }
            }
        }
        Sort sort = new Sort();
        sort.setOrders(orders);
        return sort;
    }
}
