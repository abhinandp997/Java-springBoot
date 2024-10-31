package com.crudtask.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crudtask.demo.entity.Role;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class RoleDaoImpl implements RoleDao{

    private EntityManager entityManager;

    @Autowired
    public RoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Role findRoleByName(String theRoleName) {
        
        TypedQuery<Role> query = entityManager.createQuery("from Role where name=:roleName",Role.class);
        query.setParameter("roleName", theRoleName);

        Role theRole = null;

        try {
            theRole = query.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }
        return theRole;
    }

}
