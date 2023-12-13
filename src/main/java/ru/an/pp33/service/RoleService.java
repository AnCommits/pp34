package ru.an.pp33.service;

import ru.an.pp33.models.Role;

import java.util.List;

public interface RoleService {

    void saveRole(Role role);

    Role getRoleByName(String name);

    List<Role> getAllRoles();
}
