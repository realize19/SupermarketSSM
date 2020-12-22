package com.supermarket.service.impl;

import com.supermarket.dao.MemberDao;
import com.supermarket.pojo.Member;
import com.supermarket.pojo.MemberRecord;
import com.supermarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public List<Member> getMembers(){
        return memberDao.getMembers();
    }

    @Override
    public Member getMemberBal(int memberID) {
        return memberDao.getMemberBal(memberID);
    }

    @Override
    public boolean addMember(Member param){
        //先查询是否有相同ID
        List<Member> member1=memberDao.getMember(param.getId());
        if (member1.size()>0){
            return false;
        }else {
            memberDao.addMember(param);
            return true;
        }
    }

    @Override
    public Member getMemberNPT(int memberID){
        return memberDao.getMemberNPT(memberID);
    }

    @Override
    public boolean getMemberAll(int memberID, BigDecimal total_cost) {
        return false;
    }

    @Override
    public Member getMemberID(int memberID) {
        return memberDao.getMemberID(memberID);
    }

    @Override
    public void addMemberRecord(MemberRecord memberRecord) {
        memberDao.addMemberRecord(memberRecord);
    }

    @Override
    public MemberRecord getmemberRecord(int memberID) {
        return memberDao.getmemberRecord(memberID);
    }


}
