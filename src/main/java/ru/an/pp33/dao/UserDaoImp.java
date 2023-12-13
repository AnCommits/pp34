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
import java.util.logging.Logger;

@Repository
public class UserDaoImp implements UserDao {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserById(Long id) {
        logger.info("getUserById " + id);
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
    public List<User> getUsersByRoles(List<Role> roles) {
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
    public User getUserByEmail(String email) {
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
    public void removeUserById(Long id) {
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
}
