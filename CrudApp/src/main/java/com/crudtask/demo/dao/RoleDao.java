package com.crudtask.demo.dao;

import com.crudtask.demo.entity.Role;

public interface RoleDao {

    public Role findRoleByName(String theRoleName);

}
