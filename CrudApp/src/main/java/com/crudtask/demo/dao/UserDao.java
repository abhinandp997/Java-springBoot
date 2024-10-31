package com.crudtask.demo.dao;

import java.util.List;

import com.crudtask.demo.entity.User;

public interface UserDao {

    User findByUserName(String userName);

    User findByUserId(int Id);

    List<User> findAllUser();

    void save(User user);

    void update(User user);

    void delete(int id);

}
