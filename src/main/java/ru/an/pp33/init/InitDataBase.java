package ru.an.pp33.init;

import org.springframework.stereotype.Component;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;
import ru.an.pp33.service.RoleService;
import ru.an.pp33.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InitDataBase {

    private final RoleService roleService;
    private final UserService userService;

    public InitDataBase(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        List<Role> allRoles = roleService.getAllRoles();
        if (allRoles == null || allRoles.isEmpty()) {
            roleService.saveRole(new Role("ADMIN"));
            roleService.saveRole(new Role("USER"));
        }
        if (userService.countUsers() == 0) {
            Role role1 = roleService.getRole("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role1);
            User user = new User("-", "-", "1", "1",null, roles, false);
            userService.saveUser(user);
        }
    }
}
