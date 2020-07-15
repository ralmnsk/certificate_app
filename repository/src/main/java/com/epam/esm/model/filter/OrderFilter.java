package com.epam.esm.model.filter;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderFilter extends AbstractFilter {


    private String userSurname;
    private String userName;
    private int page;


    private Long userId;
    private Long orderId;
    private Long certificateId;
}
