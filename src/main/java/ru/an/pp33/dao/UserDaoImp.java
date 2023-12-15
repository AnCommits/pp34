package ru.an.pp33.dao;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.an.pp33.models.Role;
import ru.an.pp33.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Repository
public class UserDaoImp implements UserDao {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUser(Long id) {
        logger.info("getUser(Long id) " + id);
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers");
        String hql = "FROM User";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        return query.getResultList();
    }

    @Override
    public List<User> getUsers(Role role) {
        logger.info("getUsersByRole " + role);
        String sql = "select * from users where id in " +
                "(select user_id from user_role where role_id = :roleId)";
        Query query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter("roleId", role.getId());
        List<User> users = null;
        try {
            users = query.getResultList();
        } catch (Exception e) {
            logger.info("getUsersByRole: " + e.getMessage());
        }
        return users;
    }

    @Override
    public List<User> getUsers(List<Role> roles) {
        logger.info("getUsersByRoles " + roles);
        String sql = "select * from users where id in " +
                "(select user_id from user_role where role_id in " +
                "(select id from roles where name in (:name)))";
        Query query = entityManager.createNativeQuery(sql, User.class);
        List<String> rs = roles.stream().map(Role::getName).toList();
        query.setParameter("name", rs);
        List<User> users = null;
        try {
            users = query.getResultList();
        } catch (Exception e) {
            logger.info("getUsersByRoles: " + e.getMessage());
        }
        return users;
    }

    @Override
    public User getUser(String email) {
        logger.info("getUserByEmail " + email);
        String hql = "FROM User u WHERE u.email =:email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("email", email);
        User user = null;
        try {
            user = query.getSingleResult();
            Hibernate.initialize(user.getRoles());
        } catch (Exception e) {
            logger.info("getUserByEmail: " + e.getMessage() + " " + email);
        }
        return user;
    }

    @Override
    public long saveUser(User user) {
        logger.info("updateUser " + user);
        return entityManager.merge(user).getId();
    }

    @Override
    public void removeUser(Long id) {
        logger.info("removeUserById " + id);
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public long countUsers() {
        logger.info("countUsers");
        String hql = "SELECT count(u) FROM User u";
        TypedQuery<Long> query = entityManager.createQuery(hql, Long.class);
        return query.getSingleResult();
    }

    @Override
    public void removeRoleFromUsers(Role role) {
        List<User> users = getUsers(role);
        for (User user : users) {
            Set<Role> roles = user.getRoles();
            roles.remove(new Role(role.getName()));
            saveUser(user);
        }
    }

    @Override
    public void removeRoleFromUsers(long roleId) {
        String sql = "DELETE FROM user_role WHERE role_id = :role_id";
        Query query = entityManager.createNativeQuery(sql, User.class);
        query.setParameter("role_id", roleId);
        query.executeUpdate();
    }
}
