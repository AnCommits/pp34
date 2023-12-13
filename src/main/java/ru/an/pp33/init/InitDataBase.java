package ru.an.pp33.init;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.an.pp33.constants.RolesType;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;
import ru.an.pp33.service.RoleService;
import ru.an.pp33.service.UserService;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class InitDataBase {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public InitDataBase(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        List<Role> allRoles = roleService.getAllRoles();
        if (allRoles.isEmpty()) {
            List<String> allRolesNames = RolesType.allRolesNames;
            allRolesNames.stream().map(Role::new).forEach(roleService::saveRole);
        }
        if (userService.countUsers() == 0) {
            Role role1 = roleService.getRoleByName("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role1);
            User user = new User("-", "-", "1",
                    passwordEncoder.encode("1"),
                    null, roles, false);
            userService.saveUser(user);
        }
    }
}
