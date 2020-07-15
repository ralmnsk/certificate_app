package com.epam.esm.model.filter;

import lombok.Data;

@Data
public class UserFilter extends AbstractFilter {

    private String userSurname;
    private String userName;

    private Long userId;
    private Long orderId;

}
