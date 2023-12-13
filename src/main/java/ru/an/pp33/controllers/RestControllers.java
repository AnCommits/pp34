package ru.an.pp33.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.an.pp33.models.User;

@RestController
@RequestMapping("/api/user")
public class RestControllers {

    @GetMapping("/get-me")
    public User getMyself(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
