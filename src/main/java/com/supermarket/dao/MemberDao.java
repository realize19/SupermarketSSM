package com.supermarket.dao;


import com.supermarket.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;


    public interface MemberDao {
    //会员管理-插入数据
    @Insert("Insert into tb_member(id,name,phone,points,total) valies(#{id},#{name},#{phone},#{points},#{total})")
    boolean addMember(Member param);

    //会员管理-查询所有
    @Select("select * from tb_member ")
    List<Member> getMembers();

    //会员管理-根据id查询
    @Select("select * from tb_member where id=#{id}")
    List<Member> getMember(int id);

    // 查询姓名，积分，余额
    @Select("select name,points,total from tb_member where id = #{id}")
    Member getMemberNPT(int memberID);

    // 查询会员表，获得之前的余额
    @Select("select * from tb_member where id=#{id}")
    Member getMemberBal(int MemberID);

    }