package ru.an.pp33.dao;

import ru.an.pp33.models.Role;

import java.util.List;

public interface RoleDao {

    long saveRole(Role role);

    void updateRole(Role role);

    Role getRole(long id);

    Role getRole(String name);

    List<Role> getAllRoles();

    void removeRole(long id);
}
