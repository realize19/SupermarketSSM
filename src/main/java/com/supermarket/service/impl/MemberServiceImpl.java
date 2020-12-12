package com.supermarket.service.impl;

import com.supermarket.dao.MemberDao;
import com.supermarket.pojo.Member;
import com.supermarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
