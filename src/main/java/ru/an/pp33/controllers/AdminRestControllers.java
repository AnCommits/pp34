package ru.an.pp33.controllers;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.an.pp33.dto.RoleDto;
import ru.an.pp33.dto.UserDto;
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
    private final UserService userService;
    private final RoleService roleService;
    private final UserUtils userUtils;
    private final PasswordEncoder passwordEncoder;

    public AdminRestControllers(UserService userService, RoleService roleService,
                                UserUtils userUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
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
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PutMapping("/lock/{id}")
    public void lockUser(@PathVariable long id, @RequestBody String lock) {
        User user = userService.getUserById(id);
        user.setLocked(Boolean.parseBoolean(lock));
        userService.saveUser(user);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.removeUserById(id);
    }

    @PostMapping("/save-user")
    public String saveUser(@RequestBody UserDto userDto, Authentication authentication) {
        User me = (User) authentication.getPrincipal();
        User user = UserMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setParentAdminId(me.getId());
        long id = userService.saveUser(user);
        return String.format("{\"id\": %d, \"password\": \"%s\"}", id, user.getPassword());
    }

    @PutMapping("/update-user")
    public String updateUser(@RequestBody UserDto userDto, Authentication authentication) {
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

    @PostMapping("/save-role")
    public String saveRole(@RequestBody RoleDto roleDto) {
        Role role = RoleMapper.toRole(roleDto);
        return String.valueOf(roleService.saveRole(role));
    }

    @PutMapping("/update-role")
    public void updateRole(@RequestBody RoleDto roleDto) {
        Role role = RoleMapper.toRole(roleDto);
        Role roleFromBd = roleService.getRoleById(role.getId());
        if (!roleFromBd.getName().equals("ADMIN")) {
            roleService.updateRole(role);
        }
    }
}
