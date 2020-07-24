package com.epam.esm.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserFilterDto extends AbstractFilterDto{
    private String userSurname;
    private String userName;

    private Long userId;
    private Long orderId;
}
