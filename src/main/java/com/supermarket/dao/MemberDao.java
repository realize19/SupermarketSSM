package com.supermarket.dao;

import com.supermarket.pojo.Member;
import com.supermarket.pojo.MemberRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface MemberDao {

        //查询会员表所有数据
        @Select("select * from tb_member")
        List<Member> getMembers();

        //查询是否有相同ID
        @Select("select * from tb_member where id=#{id}")
        List<Member> getMember(int id);

        // 查询会员表，获得之前的余额
        @Select("select * from tb_member where id=#{id}")
        Member getMemberBal(int MemberID);

        //插入会员信息
        @Insert("insert into tb_member (id,name,phone,points,total,register_time,update_time) values(#{id},#{name},#{phone},#{points},#{total},#{registerTime},#{updateTime})")
        boolean addMember(Member param);

        // 查询姓名，积分，余额
        @Select("select name,points,total from tb_member where id = #{id}")
        Member getMemberNPT(int memberID);

        //查询所有数据，返回的只用到了余额和积分
        @Select("select * from tb_member where id = #{id}")
        Member getMemberAll(int memberID);

        //会员结账，更新余额
        @Update("update tb_member set total=total-#{total} where id =#{id}")
        void updateMember(@Param(value = "id") int MemberID, @Param(value = "total") BigDecimal Total);

        //查询会员表，获得之前的余额
        @Select("select * from tb_member where id=#{id}")
        Member getMemberID(int memberID);

        //将数据插入到会员购物记录表中
        @Insert("insert into tb_member_record (id,member_id,user_id,order_number,sum,balance,received_points,checkout_time)values(#{id},#{memberId},#{userId},#{orderNumber},#{sum},#{balance},#{receivedPoints},#{checkoutTime})")
        void addMemberRecord(MemberRecord memberRecord);

        //查询会员购物记录表
        @Select("select * from tb_member_record where member_id = #{memberId}")
        MemberRecord getmemberRecord(int memberID);
}
