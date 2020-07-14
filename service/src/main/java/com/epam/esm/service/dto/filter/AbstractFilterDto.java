package com.epam.esm.service.dto.filter;


import com.epam.esm.page.FilterSort;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class AbstractFilterDto {
    private String tagName;

    private String certificateName;
    private String creation;
    private String modification;
    private Integer duration;
    private String description;
    private BigDecimal price;

    private String userSurname;
    private String userName;

    private Long userId;
    private Long orderId;
    private Long certificateId;
    private Long tagId;

    private int page;
    private int size;
    private FilterSort filterSort;
    private long totalPages;
    private long totalElements;


    private List<String> sortParams = new ArrayList<>();

}