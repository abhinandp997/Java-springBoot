package com.crudtask.demo.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.crudtask.demo.entity.Status;
import com.crudtask.demo.entity.User;
import com.crudtask.demo.user.WebUser;

public interface UserService extends UserDetailsService{

    public User findByUserName(String userName);

    User findByUserId(int Id);

    List<User> findAllUser();

    Status findStatusByName(String statusName);

    void save(WebUser webUser);

    void updateUser(User user);

    void approveUser(int id);

    void rejectUser(int id);

    void deleteUser(int id);

}
