package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Util.UserValidator;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.service.ServiceUser;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import javax.validation.Valid;

@Controller

public class UsersController {

    private final ServiceUser serviceUser;

    private final UsersRepository usersRepository;

    private final UserValidator userValidator;
    private final RoleRepository roleRepository;


    @Autowired
    public UsersController(ServiceUser serviceUser, UsersRepository usersRepository,
                           UserValidator userValidator, RoleRepository roleRepository) {
        this.serviceUser = serviceUser;
        this.usersRepository = usersRepository;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
    }


    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("users", serviceUser.getAll());
        model.addAttribute("user", usersRepository.findByUsername(principal.getName()));
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", serviceUser.listRoles());
        model.addAttribute("newRoles", usersRepository.findByUsername(principal.getName()).getStringUserAuthorities());

        return "users/adminAll";
    }


    @GetMapping("/user")
    public String getUser(Model model, Principal principal) {
        model.addAttribute("roles", usersRepository.findByUsername(principal.getName()).getStringUserAuthorities());
        String username = principal.getName();
        System.out.println(username);
        User user = new User();
        user = usersRepository.findByUsername(username);
        int id = user.getId();
        model.addAttribute("user", serviceUser.getUserbyId(id));
        return "users/user";
    }


    @PostMapping("/admin")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam(value = "role", required = false) List<String> roles) {
        if (roles != null) {
            int i;
            for (i = 0; i < roles.size(); i++) {
                Role role = roleRepository.findRoleByName(roles.get(i));
                user.setRoles(role);
            }
        }
        userValidator.validate(user, bindingResult);
        serviceUser.add(user);
        return "redirect:/admin";
    }

    @PatchMapping("admin/{id}")
    public String edit(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable("id") int id) {
//        System.out.println(id);
//        userValidator.validate(user, bindingResult);
        serviceUser.edit(id, user);

        return "redirect:/admin";
    }

    @DeleteMapping("admin/{id}")
    public String delete(@PathVariable("id") int id) {

        serviceUser.delete(id);

        return "redirect:/admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
