package com.crudtask.demo.dao;

import com.crudtask.demo.entity.Status;

public interface StatusDao {

    public Status findStatusByName(String status);

}
