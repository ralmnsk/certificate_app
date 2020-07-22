package com.epam.esm.web.assembler;

import com.epam.esm.dto.filter.AbstractFilterDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public interface Assembler<N, D, F extends AbstractFilterDto> {
    D assemble(N number, D dto);

    D assemble(N number, D dto, Authentication authentication);

    CollectionModel<D> toCollectionModel(F f);

    default boolean isAuthenticationAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        List<String> authorities = authentication.getAuthorities().stream().map(a -> ((GrantedAuthority) a).getAuthority()).collect(Collectors.toList());
        if (authorities.contains("ROLE_ADMIN")) {
            return true;
        }
        return false;
    }
}
