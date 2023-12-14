package ru.an.pp33.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.an.pp33.dao.RoleDao;
import ru.an.pp33.dao.UserDao;
import ru.an.pp33.models.Role;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService{

    private final RoleDao roleDao;
    private final UserDao userDao;

    public RoleServiceImp(RoleDao roleDao, UserDao userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
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
    public void removeRoleById(Long id) {
        // У всех пользователей с ролью с данным id удалить роль.

        // Получаем список пользователей с набором ролей, хотя нужно лишь с одной ролью,
        // т.к. метод getUsersByRoles(List<Role> roles) уже написан.
        Role role = roleDao.getRoleById(id);
        Role role = new Role();
        role.ad
        List<Role> roles = new ArrayList<>();
        roles.add(new Role());
        userDao.getUsersByRoles()
        roleDao.removeRoleById(id);
    }
}
