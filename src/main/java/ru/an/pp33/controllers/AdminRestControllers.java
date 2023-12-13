package ru.an.pp33.controllers;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.an.pp33.constants.RolesType;
import ru.an.pp33.dto.FrontUser;
import ru.an.pp33.helper.UserUtils;
import ru.an.pp33.mapper.UserMapper;
import ru.an.pp33.models.User;
import ru.an.pp33.service.UserService;

import java.util.List;

@Data
@RestController
@RequestMapping("/admin/api")
public class AdminRestControllers {
    private final UserService userService;
    private final UserUtils userUtils;
    private final PasswordEncoder passwordEncoder;

    public AdminRestControllers(UserService userService, UserUtils userUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userUtils = userUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get-all-users")
    public List<User> getAllUsers(Authentication authentication) {
        List<User> users = userService.getAllUsers();
        User me = (User) authentication.getPrincipal();
//  ToDo                        isAncestor достаточно устанавливать только для админов
        users.forEach(u -> u.setDescendant(userUtils.isAncestor(u, me)));
        return users;
    }

    @GetMapping("/all-roles")
    public List<String> getAllRoles() {
        return RolesType.allRolesNames;
    }

    @PutMapping("/lock/{id}")
    public void lockUser(@PathVariable long id, @RequestBody String lock) {
        User user = userService.getUserById(id);
        user.setLocked(Boolean.parseBoolean(lock));
        userService.saveUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.removeUserById(id);
    }

    @PostMapping("/save-user")
    public String saveUser(@RequestBody FrontUser userDto, Authentication authentication) {
        User me = (User) authentication.getPrincipal();
        User user = UserMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setParentAdminId(me.getId());
        long id = userService.saveUser(user);
        return String.format("{\"id\": %d, \"password\": \"%s\"}", id, user.getPassword());
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody FrontUser userDto, Authentication authentication) {
        User me = (User) authentication.getPrincipal();
        User user = UserMapper.toUser(userDto);
        String oldPassword = userService.getUserById(user.getId()).getPassword();
        if (!oldPassword.equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
// ToDo                              Check admin's rights about other admins
        userUtils.setUsersParentAdminId(user, me);
        userService.saveUser(user);
        return user.getPassword();
    }
}
