package org.example.controller;

import org.example.UserService;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;


@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(){
        return "/registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model){
        User userFromOb = userRepo.findByUsername(user.getUsername()).orElse(null);
        if (userFromOb != null){
            model.put("message", "User exists!");
            return "registration";
        }
        user.setActive(true);
//        user.setRoles(Collections.singleton(Role.USER));
        System.out.println("Пользователь: " + user.getUsername() + ", пароль: " + user.getPassword());
        userService.saveUser(user);
        return "redirect:/login";
    }

}