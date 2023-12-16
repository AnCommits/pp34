package ru.an.pp33.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable("users")
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    @Cacheable("users")
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Cacheable("users")
    public List<User> getUsers(Role role) {
        return userDao.getUsers(role);
    }

    @Override
    @Cacheable("users")
    public List<User> getUsers(List<Role> roles) {
        return userDao.getUsers(roles);
    }

    @Override
    @Cacheable("users")
    public User getUser(String email) {
        return userDao.getUser(email);
    }

    @Transactional
    @Override
    @CachePut("users")
    public long saveUser(User user) {
        if (user != null) {
            Set<Role> roles = new HashSet<>();
            for (Role role : user.getRoles()) {
                Role roleFromDb = roleDao.getRole(role.getName());
                roles.add(roleFromDb);
            }
            user.setRoles(roles);
            return userDao.saveUser(user);
        }
        return -1;
    }

    @Transactional
    @Override
    @CacheEvict("users")
    public User removeUser(Long id) {
        return userDao.removeUser(id);
    }

    @Override
    public long countUsers() {
        return userDao.countUsers();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUser(email);
        if (user == null) {
            throw new UsernameNotFoundException(email + " not found");
        }
        return user;
    }
}
