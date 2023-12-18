package ru.an.pp33.controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.an.pp33.dto.RoleDto;
import ru.an.pp33.dto.UserDto;
import ru.an.pp33.errors.AppError;
import ru.an.pp33.helper.UserUtils;
import ru.an.pp33.mapper.RoleMapper;
import ru.an.pp33.mapper.UserMapper;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;
import ru.an.pp33.service.RoleService;
import ru.an.pp33.service.UserService;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/admin")
public class AdminRestControllers {
    private final static String USERS_NOT_FOUND = "Пользователи не найдены!";
    private final static String USER_NOT_FOUND = "Пользователь не найдены!";
    private final static String ROLES_NOT_FOUND = "Роли не найдены!";
    private final static String ROLE_NOT_FOUND = "Роли не найдены!";

    private final static String INTERNAL_ERROR = "Внутренняя ошибка сервера";

    private final UserService userService;
    private final RoleService roleService;
    private final UserUtils userUtils;

    public AdminRestControllers(UserService userService, RoleService roleService, UserUtils userUtils) {
        this.userService = userService;
        this.roleService = roleService;
        this.userUtils = userUtils;
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        List<User> users = userService.getAllUsers();
        User me = (User) authentication.getPrincipal();
        if (users != null) {
            users.forEach(u -> u.setDescendant(userUtils.isAncestor(u, me)));
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), USERS_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all-roles")
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roles != null
                ? new ResponseEntity<>(roles, HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), ROLES_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> lockUser(@PathVariable long id, @RequestBody String lock) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        user.setLocked(Boolean.parseBoolean(lock));
        return userService.saveUser(user) > 0
                ? new ResponseEntity<>("", HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        User user = userService.removeUser(id);
        return user != null
                ? new ResponseEntity<>("", HttpStatus.OK)
                : new ResponseEntity<>(
                new AppError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/save-user")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto, Authentication authentication) {
        User me = (User) authentication.getPrincipal();
        User user = UserMapper.toUser(userDto);
        user.setParentAdminId(me.getId());
        long id = userService.saveUser(user);
        return id > 0
                ? new ResponseEntity<>(
                String.format("{\"id\": %d, \"password\": \"%s\"}", id, user.getPassword()), HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, Authentication authentication) {
        User me = (User) authentication.getPrincipal();
        User user = UserMapper.toUser(userDto);
        if (!userUtils.setUsersParentAdminId(user, me)) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        return userService.updateUser(user) > 0
                ? new ResponseEntity<>(user.getPassword(), HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/save-role")
    public ResponseEntity<?> saveRole(@RequestBody RoleDto roleDto) {
        Role role = RoleMapper.toRole(roleDto);
        long id = roleService.saveRole(role);
        return id > 0
                ? new ResponseEntity<>(String.valueOf(id), HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto) {
        Role roleFromBd = roleService.getRole(roleDto.getId());
        if (roleFromBd == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    ROLE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (roleFromBd.getName().equals("ADMIN")) {
            String message = "Отказ в переименовании роли ADMIN!";
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                    message), HttpStatus.FORBIDDEN);
        }
        Role role = RoleMapper.toRole(roleDto);
        return roleService.updateRole(role) != null
                ? new ResponseEntity<>("", HttpStatus.OK)
                : new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete-role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable long id) {
        Role roleFromBd = roleService.getRole(id);
        if (roleFromBd == null) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    ROLE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        String message = "Отказ в удалении роли ADMIN!";
        return roleFromBd.getName().equals("ADMIN")
                ? new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                message), HttpStatus.FORBIDDEN)
                : new ResponseEntity<>("", HttpStatus.OK);
    }
}
