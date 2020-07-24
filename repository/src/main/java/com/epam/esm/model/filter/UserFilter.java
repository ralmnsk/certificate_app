package com.epam.esm.model.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserFilter extends AbstractFilter {

    private String userSurname;
    private String userName;

    private Long userId;
    private Long orderId;

}
