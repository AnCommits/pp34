package ru.an.pp33.service;

import ru.an.pp33.models.Role;

import java.util.List;

public interface RoleService {

    long saveRole(Role role);

    void updateRole(Role role);

    Role getRoleById(long id);

    Role getRoleByName(String name);

    List<Role> getAllRoles();

    void removeRole(Role role);
}
