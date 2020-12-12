package com.supermarket.service;

import com.supermarket.pojo.User;

public interface UserService {

    //用户登录

    User login(String username, String password, Integer role);
}
