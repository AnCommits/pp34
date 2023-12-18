package ru.an.pp33.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<User> getUsers(Role role) {
        return userDao.getUsers(role);
    }

    @Override
    public List<User> getUsers(List<Role> roles) {
        return userDao.getUsers(roles);
    }

    @Override
    public User getUser(String email) {
        return userDao.getUser(email);
    }

    @Transactional
    @Override
    public long saveUser(User user, boolean... methodUpdate) {
        if (user != null) {
            Set<Role> roles = new HashSet<>();
            for (Role role : user.getRoles()) {
                Role roleFromDb = roleDao.getRole(role.getName());
                roles.add(roleFromDb);
            }
            user.setRoles(roles);

            if (methodUpdate.length == 0) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                String oldPassword = userDao.getUser(user.getId()).getPassword();
                if (!oldPassword.equals(user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }

            return userDao.saveUser(user);
        }
        return -1;
    }

    @Transactional
    @Override
    public long updateUser(User user) {
        return saveUser(user, true);
    }

    @Transactional
    @Override
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
