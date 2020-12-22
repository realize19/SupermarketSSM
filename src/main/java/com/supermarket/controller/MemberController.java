package com.supermarket.controller;

import com.alibaba.fastjson.JSON;
import com.supermarket.pojo.IDUtil;
import com.supermarket.pojo.Member;
import com.supermarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value="/member" )
public class MemberController {

        @Autowired
        private MemberService memberService;

    //添加会员
    @RequestMapping(value ="/addMember")
    public String addMember(Model model, Member param){
            long currentTime = System.currentTimeMillis();
            String forwardPage;
            param.setRegisterTime(currentTime);
            param.setUpdateTime(currentTime);
            boolean aBollean=memberService.addMember(param);
            if(aBollean){
                forwardPage= "manager";
            }else{
                forwardPage= "error";
            }
            //刷新会员界面
            List<Member> list = memberService.getMembers();
            model.addAttribute("members",list);
            return forwardPage;
        }


    // 余额结账--输入卡号查询出姓名，积分，余额
    @RequestMapping(value = "/getMember")
    @ResponseBody
    public String showMember(int memberID){
        Member member = memberService.getMemberNPT(memberID);
        String data = JSON.toJSONString(member);
        return data;
    }

    // 返回按钮-到收银界面
    @RequestMapping(value = "/return")
    private String Return(Model model){
        //进入收银
        Integer shoppingNum = IDUtil.getId();
        model.addAttribute("shoppingNum",shoppingNum);
        return "cashier";
    }

}
