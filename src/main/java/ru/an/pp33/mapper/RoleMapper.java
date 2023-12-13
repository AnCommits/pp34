package ru.an.pp33.mapper;

import ru.an.pp33.dto.RoleDto;
import ru.an.pp33.models.Role;

public class RoleMapper {

    public static Role toRole(RoleDto roleDto) {
        Role role = new Role(roleDto.getName());
        role.setId(roleDto.getId());
        return role;
    }
}
