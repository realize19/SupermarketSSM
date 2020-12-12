package com.supermarket.service.impl;


import com.supermarket.dao.UserDao;
import com.supermarket.pojo.User;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String username, String password, Integer role) {
        User user= userDao.findWithLoginAndPassword(username,password,role);
        return user;
    }
}

