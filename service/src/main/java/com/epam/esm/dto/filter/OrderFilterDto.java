package com.epam.esm.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class OrderFilterDto extends AbstractFilterDto {
    private String certificateName;

    private String userSurname;
    private String userName;
    private int page;

    private Long userId;
    private Long orderId;
    private Long certificateId;
}
