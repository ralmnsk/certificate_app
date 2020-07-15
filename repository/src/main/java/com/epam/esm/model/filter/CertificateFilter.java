package com.epam.esm.model.filter;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CertificateFilter extends AbstractFilter {
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
}
