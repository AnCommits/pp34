package ru.an.pp33.dao;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import ru.an.pp33.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long saveRole(Role role) {
        entityManager.persist(role);
        return getRoleByName(role.getName()).getId();
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
//        String sql = "UPDATE roles SET name =:name WHERE name =:oldname";
//        Query nativeQuery = entityManager.createNativeQuery(sql);
//        nativeQuery.setParameter("name", role.getName());
//        nativeQuery.setParameter("oldname", "123");
//        int i = nativeQuery.executeUpdate();
//        System.out.println("------------------------ " + i);
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
        String hql = "FROM Role r ORDER BY r.id";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        return query.getResultList();
    }
}
