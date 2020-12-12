package com.supermarket.service;

import com.supermarket.pojo.Member;
import org.springframework.stereotype.Component;

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



}
