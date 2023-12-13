package ru.an.pp33.dao;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import ru.an.pp33.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RolesDaoImp implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role getRoleByName(String name) {
        String hql = "FROM Role r WHERE r.name =:name";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        query.setParameter("name", name);
        Role role = null;
        try {
            role = query.getSingleResult();
//            Hibernate.initialize(role.getUsers());
        } catch (Exception ignored) {
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        String hql = "FROM Role";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        return query.getResultList();
    }
}
