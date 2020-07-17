//package com.epam.esm.web.generator;
//
//import com.epam.esm.model.Role;
//import com.epam.esm.service.dto.UserDto;
//import com.epam.esm.service.user.UserService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController("/")
//public class AdminService {
//    private UserService userService;
//
//    public AdminService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/setadmin/{login}")
//    public void setAdmin(@PathVariable(required = true) String login) {
//        UserDto user = userService.findByLogin(login);
//        if (user != null) {
//            user.setRole(Role.ADMIN);
//            userService.update(user);
//        }
//    }
//
//    @GetMapping("/setuser/{login}")
//    public void setUser(@PathVariable(required = true) String login) {
//        UserDto user = userService.findByLogin(login);
//        if (user != null) {
//            user.setRole(Role.USER);
//            userService.update(user);
//        }
//    }
//
//
//}
