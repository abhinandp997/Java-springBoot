package com.crudtask.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crudtask.demo.entity.Status;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class StatusDaoImpl implements StatusDao{

    private EntityManager entityManager;

    @Autowired
    public StatusDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Status findStatusByName(String statusName) {
        TypedQuery<Status> query = entityManager.createQuery("from Status where statusName = :statusName", Status.class);
        query.setParameter("statusName", statusName); 
        Status statuss = query.getSingleResult();
        return statuss;
    }


}
