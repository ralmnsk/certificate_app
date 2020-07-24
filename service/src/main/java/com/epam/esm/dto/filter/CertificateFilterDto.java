package com.epam.esm.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CertificateFilterDto extends AbstractFilterDto {
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
