package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/users";
    }

    @GetMapping("/id")
    public String show1(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "/show";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "/new";
    }

    @PostMapping
    public String create(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "/new";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/change/id")
    public String changeUser1(Model model, @RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        model.addAttribute("user", userService.getUser(id));
        return "/change";
    }

    @PostMapping("change/id")
    public String change1(@Valid User user, BindingResult result, @RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        if (result.hasErrors()) {
            return "/change";
        }
        userService.editUser(id, user);
        return "redirect:/users";
    }

    @PostMapping("delete/id")
    public String deleteUser(@RequestParam(value = "id", required = false, defaultValue = "0") Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
