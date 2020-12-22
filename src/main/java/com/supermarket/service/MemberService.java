package com.supermarket.service;

import com.supermarket.pojo.Member;
import com.supermarket.pojo.MemberRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public interface MemberService {
    //会员查询所有接口
    List<Member> getMembers();

    //查询会员表，获得之前的余额
    Member getMemberBal(int memberID);

    //会员添加接口
    boolean addMember(Member param);

    // 查询指定会员数据
    Member getMemberNPT(int memberID);


    //查询会员表，判断余额是否足够(不足提示，足够更新会员表)
    boolean getMemberAll(int memberID, BigDecimal total_cost);

    //查询会员表，获得之前的余额
    Member getMemberID(int memberID);

    //插入会员购物记录表
    void addMemberRecord(MemberRecord memberRecord);

    //查询会员购物记录表
    MemberRecord getmemberRecord(int memberID);
}
