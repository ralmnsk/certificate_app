package com.epam.esm.repository.jpa;

import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
