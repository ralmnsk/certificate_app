package com.epam.esm.service.dto;


import com.epam.esm.page.Sort;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class FilterDto {
    private String tagName;
    private String certificateName;
    private String creation;
    private String modification;
    private Integer duration;
    private String description;
    private BigDecimal price;
    private String userSurname;
    private String userName;
    private int page;
    private int size;
    private Sort sort;
    private Long userId;
    private Long orderId;
    private Long certificateId;
    private Long tagId;
    private long totalPages;
    private long totalElements;


    private List<String> sortParams = new ArrayList<>();

}