package ru.an.pp33.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.an.pp33.dao.RoleDao;
import ru.an.pp33.dao.UserDao;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.List;
import java.util.Set;

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
    public Role updateRole(Role role) {
        return roleDao.updateRole(role);
    }

    @Override
    public Role getRole(long id) {
        return roleDao.getRole(id);
    }

    @Override
    public Role getRole(String name) {
        return roleDao.getRole(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional
    @Override
    public void removeRole(Role role) {
        userDao.removeRoleFromUsers(role);
        roleDao.removeRole(role.getId());
    }

    @Transactional
    @Override
    public void removeRole(long id) {
        userDao.removeRoleFromUsers(id);
        roleDao.removeRole(id);
    }
}
