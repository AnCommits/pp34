package ru.an.pp33.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.an.pp33.dao.RoleDao;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService{

    private final RoleDao roleDao;
    private final UserService userService;

    public RoleServiceImp(RoleDao roleDao, UserService userService) {
        this.roleDao = roleDao;
        this.userService = userService;
    }

    @Transactional
    @Override
    public long saveRole(Role role) {
        return roleDao.saveRole(role);
    }

    @Transactional
    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public Role getRoleById(long id) {
        return roleDao.getRoleById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDao.getRoleByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional
    @Override
    public void removeRoleById(Role role) {
    // 1-й шаг - У всех пользователей с ролью role удалить роль:
    //   1-й вариант - получить из БД пользователей с ролью, удалить у каждого роль, сохранить пользователей в БД.
    //   2-й вариант - в таблице user_role удалить все записи с нужным role_id.
    // 2-й шаг - удалить роль из БД.

        // Получаем список пользователей с набором ролей, хотя достаточно лишь с одной ролью,
        // т.к. метод getUsersByRoles(List<Role> roles) уже написан.
        // 1-й вариант
        List<User> users = userService.getUsersByRoles(List.of(role));
        for (User user : users) {
            Set<Role> roles = user.getRoles();
            roles.remove(new Role(role.getName()));
            userService.saveUser(user);
        }
        roleDao.removeRoleById(role.getId());
    }
}
