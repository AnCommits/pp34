package ru.an.pp33.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.an.pp33.dao.RoleDao;
import ru.an.pp33.models.Role;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService{

    private final RoleDao roleDao;

    public RoleServiceImp(RoleDao roleDao) {
        this.roleDao = roleDao;
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
}
