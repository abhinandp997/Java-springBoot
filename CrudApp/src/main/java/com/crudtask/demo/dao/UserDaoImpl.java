package com.crudtask.demo.dao;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crudtask.demo.entity.Role;
import com.crudtask.demo.entity.Status;
import com.crudtask.demo.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class UserDaoImpl implements UserDao{

    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUserName(String userName) {
        TypedQuery<User> query = entityManager.createQuery("from User where userName=:userName and enabled=true",User.class);
        query.setParameter("userName", userName);

        User theUser = null;

        try {
            theUser = query.getSingleResult();
        } catch (Exception e) {
            theUser = null;
        }
        return theUser;
    }

    // @Override
    // public User findByUserId(int Id) {
        
    //     TypedQuery<User> query = entityManager.createQuery("select u from User u "+"JOIN FETCH u.roles "+"where u.id = :data",User.class);
    //     query.setParameter("data", Id);

    //     User user = query.getSingleResult();

    //     return user;
    // }
    @Override
    public User findByUserId(int id) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u JOIN FETCH u.roles where u.id = :data", User.class);
        query.setParameter("data", id);
        User user = null;
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
            // Log the exception and handle it gracefully
            logger.info("No user found with ID: " + id+" "+ e);
        }
        return user;
    }


    @Override
    public List<User> findAllUser() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);

        List<User> users = query.getResultList();

        return users;
    }

    @Override
    @Transactional
    public void save(User user) {
       entityManager.persist(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }

    // @Override
    // @Transactional
    // public void delete(int id) {
    //     logger.info("Inside delete....");
    //     User user = entityManager.find(User.class, id);
    //     logger.info("deleting User "+user.getUserName());
    //     if (user != null) {
    //         entityManager.remove(user);
    //         logger.info("User deleted: " + id);
    //     } else {
    //         logger.info("User not found with ID: " + id);
    //     }
    // }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void delete(int id){
        logger.info("Inside delete....");
        User user = entityManager.find(User.class, id);
        if(user != null){
            logger.info("Deleting user: " + user.getUserName());
            //remove user from roles
            for(Role role:user.getRoles()){
                role.getUsers().remove(user);
            }
            //remove user from status
            for(Status status: user.getStatus()){
                status.getUsers().remove(user);
            }
            user.getRoles().clear();
            user.getStatus().clear();
            entityManager.remove(user);
        }
    }
}
