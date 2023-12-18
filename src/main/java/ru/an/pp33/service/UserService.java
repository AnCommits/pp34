package ru.an.pp33.service;

import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.List;

public interface UserService {

    long saveUser(User user, boolean... methodUpdate);

    long updateUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    List<User> getUsers(Role role);

    /**
     * @return list of users that have any role in the list
     */
    List<User> getUsers(List<Role> roles);

    User getUser(String email);

    User removeUser(Long id);

    long countUsers();
}
