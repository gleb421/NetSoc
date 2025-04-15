package org.example.controller;

import org.example.domain.User;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public class TestDBController {
    @RestController
    @RequestMapping("/api/test")
    public class TestDbController {

        @Autowired
        private UserRepository userRepository;

        @GetMapping("/users")
        public List<User> getUsers() {
            return userRepository.findAll();
        }
    }

}
