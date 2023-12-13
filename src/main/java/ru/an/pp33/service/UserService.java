package ru.an.pp33.service;

import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.List;

public interface UserService {

    long saveUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    List<User> getUsersByRoles(List<Role> roles);

    User getUserByEmail(String email);

    void removeUserById(Long id);

    long countUsers();
}
