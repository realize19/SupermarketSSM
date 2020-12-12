package com.supermarket.dao;


import com.supermarket.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    @Select("select * from tb_user where username=#{username} and password=#{password} and role=#{role}")
    User findWithLoginAndPassword(@Param("username")String userName,@Param("password")String password, @Param("role") Integer role);
}
