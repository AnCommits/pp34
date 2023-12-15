package ru.an.pp33.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.an.pp33.dao.RoleDao;
import ru.an.pp33.dao.UserDao;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserServiceImp(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userDao.getUsersByRole(role);
    }

    /**
     * @return list of users that have any role in the list
     */
    @Override
    public List<User> getUsersByRoles(List<Role> roles) {
        return userDao.getUsersByRoles(roles);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Transactional
    @Override
    public long saveUser(User user) {
        if (user != null) {
            Set<Role> roles = new HashSet<>();
            for (Role role : user.getRoles()) {
                Role roleFromDb = roleDao.getRoleByName(role.getName());
                roles.add(roleFromDb);
            }
            user.setRoles(roles);
            return userDao.saveUser(user);
        }
        return -1;
    }

    @Transactional
    @Override
    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }

    @Override
    public long countUsers() {
        return userDao.countUsers();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email + " not found");
        }
        return user;
    }
}
