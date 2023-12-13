package ru.an.pp33.mapper;

import ru.an.pp33.dto.UserDto;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        Set<Role> roles = new HashSet<>();
        userDto.getRoles().forEach(r -> roles.add(new Role(r)));
        User user = new User(
                userDto.getFirstname(),
                userDto.getLastname(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getBirthdate(),
                roles,
                userDto.isLocked()
        );
        user.setId(userDto.getId());
        return user;
    }
}
