package ru.an.pp33.dao;

import ru.an.pp33.models.Role;

import java.util.List;

public interface RoleDao {

    long saveRole(Role role);

    Role getRoleByName(String name);

    List<Role> getAllRoles();
}
