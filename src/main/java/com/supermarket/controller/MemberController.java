package com.supermarket.controller;

import com.supermarket.pojo.Member;
import com.supermarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value="/Member")
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

}
