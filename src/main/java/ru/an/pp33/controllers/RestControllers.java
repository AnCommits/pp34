package ru.an.pp33.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.an.pp33.errors.AppError;
import ru.an.pp33.models.User;

@RestController
@RequestMapping("/api/user")
public class RestControllers {

    @GetMapping("/get-me")
    public ResponseEntity<?> getMyself(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String message = "Этой ошибки не может быть!";
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
