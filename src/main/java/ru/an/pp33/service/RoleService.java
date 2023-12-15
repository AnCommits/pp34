package ru.an.pp33.service;

import ru.an.pp33.models.Role;

import java.util.List;

public interface RoleService {

    long saveRole(Role role);

    void updateRole(Role role);

    Role getRole(long id);

    Role getRole(String name);

    List<Role> getAllRoles();

    void removeRole(Role role);

    void removeRole(long id);
}
