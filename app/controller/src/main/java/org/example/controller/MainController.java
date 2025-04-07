package org.example.controller;

import org.example.UserService;
import org.example.domain.User;
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

    @GetMapping("/chat")
    public String chatPage(Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        return "chat"; // имя файла chat.html в templates
    }

    @GetMapping("/home")
    public String home(Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            // например, вытаскиваем из Principal и добавляем в сессию
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                currentUser = userService.findByUsername(auth.getName()).orElse(null);
                if (currentUser != null) {
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
        System.out.println("Friend name: " + friendName); // Изменил print на println для лучшей видимости
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser == null) {
            model.addAttribute("message", "User not logged in!");
            return "home";
        }
        if (userService.findByUsername(friendName).isPresent()) {
            userService.addFriendManually(currentUser.getId(), userService.findByUsername(friendName).get().getId());
            model.addAttribute("message", "Friend added successfully!");
            return "home";
        }

        model.addAttribute("message", "Friend not found!");
        return "home";
    }

    @GetMapping("/api/current-user")
    @ResponseBody
    public User getCurrentUser(@ModelAttribute("currentUser") User currentUser) {
        return currentUser;
    }

    @GetMapping("/api/friends")
    @ResponseBody
    public Set<User> getFriends(@ModelAttribute("currentUser") User currentUser) {
        if (currentUser == null) {
            throw new RuntimeException("User not in session");
        }
        return userService.getFriends(currentUser.getId());
    }
}
