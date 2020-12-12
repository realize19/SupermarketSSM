package com.supermarket.controller;

import com.supermarket.pojo.IDUtil;
import com.supermarket.pojo.Member;
import com.supermarket.pojo.User;
import com.supermarket.service.MemberService;
import com.supermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 登录
 */
@Controller
@RequestMapping(value = "/supermarket")
public class UserController {
   @Autowired
   public UserService userService;
   @Resource
   private MemberService memberService;
   @RequestMapping(value = "index", method = RequestMethod.POST)
   public String login(String username, String password, Integer role, Model model,HttpSession session) {
      // 调用service方法
      User user = userService.login(username, password, role);
      session.setAttribute("user", user);
      if (1 == user.getRole()) {
         //进入会员管理界面
         List<Member> list =memberService.getMembers();
         model.addAttribute("members",list);
         return "manager";
      } else if (2 == user.getRole()) {

         model.addAttribute("shoppingNum", IDUtil.getId());

         return "cashier";

      }
      return "error";

   }
   }


