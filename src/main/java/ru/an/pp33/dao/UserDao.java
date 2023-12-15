package ru.an.pp33.dao;

import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.List;

public interface UserDao {

    long saveUser(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    List<User> getUsers(Role role);

    List<User> getUsers(List<Role> roles);

    User getUser(String email);

    User removeUser(Long id);

    long countUsers();

    void removeRoleFromUsers(Role role);

    void removeRoleFromUsers(long roleId);
}
