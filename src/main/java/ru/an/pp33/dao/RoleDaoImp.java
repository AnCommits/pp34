package ru.an.pp33.dao;

import org.springframework.stereotype.Repository;
import ru.an.pp33.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long saveRole(Role role) {
        entityManager.persist(role);
        return getRole(role.getName()).getId();
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public Role getRole(long id) {
        String hql = "FROM Role r WHERE r.id =:id";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        query.setParameter("id", id);
        Role role = null;
        try {
            role = query.getSingleResult();
//            Hibernate.initialize(role.getUsers());
        } catch (Exception ignored) {
        }
        return role;
    }

    @Override
    public Role getRole(String name) {
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
        String hql = "FROM Role r ORDER BY r.id";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        return query.getResultList();
    }

    @Override
    public void removeRole(long id) {
        Role role = entityManager.find(Role.class, id);
        entityManager.remove(role);
    }
}
