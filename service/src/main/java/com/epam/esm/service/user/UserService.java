package com.epam.esm.service.user;


import com.epam.esm.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService<T, E> extends CrudService<T, E> {

//    Page<T> getAll(Pageable pageable);

//    Optional<T> getByName(String name);
}
