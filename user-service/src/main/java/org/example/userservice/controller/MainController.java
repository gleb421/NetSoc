package org.example.userservice.controller;

import jakarta.servlet.http.HttpSession;

import org.example.userservice.dto.UserDto;
import org.example.userservice.model.User;
import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@SessionAttributes("currentUser")
public class MainController {

    private final UserService userService;

    public MainController(@Autowired UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/home")
    public String home(Model model) {
        UserDto currentUser = (UserDto) model.getAttribute("currentUser");

        if (currentUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                User userEntity = userService.findByUsername(auth.getName()).orElse(null);
                if (userEntity != null) {
                    UserDto tmp = new UserDto(userEntity.getId(), userEntity.getUsername());
                    currentUser = tmp;
                    model.addAttribute("currentUser", currentUser);
                }
            }
        }

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("message", "Добро пожаловать, " + currentUser.getUsername() + "!");
        return "home";
    }

    @PostMapping("/home")
    public String addFriend(@RequestParam("friendName") String friendName, Model model) {
        UserDto currentUser = (UserDto) model.getAttribute("currentUser");

        if (currentUser == null) {
            model.addAttribute("message", "User not logged in!");
            return "home";
        }

        return userService.findByUsername(friendName)
                .map(friend -> {
                    userService.addFriendManually(currentUser.getId(), friend.getId());
                    model.addAttribute("message", "Friend added successfully!");
                    return "home";
                })
                .orElseGet(() -> {
                    model.addAttribute("message", "Friend not found!");
                    return "home";
                });
    }

    @GetMapping("/api/current-user")
    @ResponseBody
    public UserDto currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                return new UserDto(user.getId(), user.getUsername());
            }
        }

        return null;
    }
    @GetMapping("/api/users/{id}/friends")
    @ResponseBody
    public UserDto[] getFriendsById(@PathVariable Long id) {
        Set<User> friends = userService.getFriends(id);

        return friends.stream()
                .map(user -> new UserDto(user.getId(), user.getUsername()))
                .toArray(UserDto[]::new);
    }

}
