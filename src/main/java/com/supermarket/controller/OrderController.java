package com.supermarket.controller;

import com.supermarket.pojo.*;
import com.supermarket.service.CommodityService;
import com.supermarket.service.MemberService;
import com.supermarket.service.OrderItemService;
import com.supermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    @Qualifier("orderService")
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private MemberService memberService;

    //现金结账
    @RequestMapping(value = "/checkoutByCash")
    public String checkoutByCash(Model model, String shoppingNum, String total_cost, String category, String cash_receive, String cash_balance, Order order) {
        String forwardPage;
        //首先获得orderitem表中相同流水号未结账的数量
        List<OrderItem> orderChecked = orderItemService.getOrderChecked(shoppingNum);
        for (OrderItem item : orderChecked) {
            // 更新购物车表 循环遍历每一条的数量和库存，进行计算,顺便把结账状态改为1
            int data = orderItemService.updateOrderStock(item.getId(), item.getCount());
            // 判断更新的条数
            if (data <= 0) {
                forwardPage = "error";
            }
        }
        // 再查一遍购物车表，得到结账更新后的Stock
        List<OrderItem> orderStcok = orderItemService.getOrderYChecked(shoppingNum);
        // 更新库存表-将返回的data更新到库存中
        for (OrderItem item : orderStcok) {
            commodityService.updateCommodityStock(item.getCommodity_id(), item.getStock());
        }

        // 执行插入，将现金结账输入的内容插入到Order（结账）表，先定义要插入的字段
        // id-随机数
        int id = IDUtil.getId();
        // 收到的钱-共计
        BigDecimal sum = new BigDecimal(total_cost);
        // 收银员ID
        int CashierID = 0001;
        // 会员ID
        int memberID = 0;
        // 结账形式
        int CheckeoutType = 1;
        // 结账时间
        long currentTime = System.currentTimeMillis();

        order.setId(id);
        order.setOrderNumber(shoppingNum);
        order.setSum(sum);
        order.setUserId(CashierID);
        order.setMemberId(memberID);
        order.setCheckoutType(CheckeoutType);
        order.setCheckoutTime(currentTime);
        // 将数据添加到结账表中
        boolean orderFlag = orderService.addOrder(order);
        if (orderFlag == true) {
            forwardPage = "receipt";
        } else {
            forwardPage = "receipt";
        }
        model.addAttribute("category", category);
        // 合计----√
        model.addAttribute("total_cost", sum);
        // 收款----X
        model.addAttribute("cash_receive", cash_receive);
        // 找零--X
        model.addAttribute("cash_balance", cash_balance);
        // 积分ID
        model.addAttribute("member_id", 0);
        // 本次积分
        model.addAttribute("member_current_points", 0);
        //累计积分
        model.addAttribute("member_points", 0);
        // 流水号----√
        model.addAttribute("shoppingNum", shoppingNum);

        return "receipt";
    }

    //余额结账
    @RequestMapping(value = "/checkoutByMember")
    public String checkoutByMember(Model model, String shoppingNum, int memberID, BigDecimal total_cost, int category, BigDecimal cash_receive, BigDecimal cash_balance) {
        String forwardPage;
        //首先获得orderitem表中相同流水号未结账的数量
        List<OrderItem> orderChecked = orderItemService.getOrderChecked(shoppingNum);
        for (OrderItem item : orderChecked) {
            // 更新购物车表 循环遍历每一条的数量和库存，进行计算,顺便把结账状态改为1
            int data = orderItemService.updateOrderStock(item.getId(), item.getCount());
            // 判断更新的条数
            if (data <= 0) {
                forwardPage = "error";
            }
        }
        // 再查一遍购物车表，得到结账更新后的Stock
        List<OrderItem> orderStcok = orderItemService.getOrderYChecked(shoppingNum);
        // 更新库存表-将返回的data更新到库存中
        for (OrderItem item : orderStcok) {
            commodityService.updateCommodityStock(item.getCommodity_id(), item.getStock());
        }
        // 查询会员表，获得之前的余额
        Member member = memberService.getMemberBal(memberID);
        // 查询会员表，判断余额是否足够
        boolean Banlence = memberService.getMemberAll(memberID, total_cost);
        if (Banlence == false) {
            forwardPage = "error";
        }
        // 查询更新后的会员表
        Member members = memberService.getMemberID(memberID);

        // 最后将查出来的数据插入到会员购物记录表中
        MemberRecord memberRecord = new MemberRecord();
        memberRecord.setId(IDUtil.getId());
        memberRecord.setMemberId(memberID);
        memberRecord.setUserId(0001);
        memberRecord.setOrderNumber(category);
        memberRecord.setSum(member.getTotal());
        memberRecord.setBalance(members.getTotal());
        memberRecord.setReceivedPoints(member.getPoints());
        memberRecord.setCheckoutTime(System.currentTimeMillis());
        //最后将查出来的数据插入到会员购物记录表中
        memberService.addMemberRecord(memberRecord);
        //最后插入到小票表
        Order order = new Order();
        order.setId(IDUtil.getId());
        order.setOrderNumber(shoppingNum);
        order.setSum(total_cost);
        order.setUserId(0001);
        order.setMemberId(memberID);
        order.setCheckoutType(1);
        order.setCheckoutTime(System.currentTimeMillis());
        // 将数据添加到结账表中
        boolean orderFlag = orderService.addOrder(order);
        if(orderFlag== true){
            forwardPage = "receipt";
        }else{
            forwardPage = "receipt";
        }
        // 查询最新的会员购物记录表
        MemberRecord memberRecord1 = memberService.getmemberRecord(memberID);
        // 共有？条----X
        model.addAttribute("category",category);
        // 合计----√
        model.addAttribute("total_cost",total_cost);
        // 收款----X
        model.addAttribute("cash_receive",cash_receive);
        // 找零--X
        model.addAttribute("cash_balance",cash_balance);
        // 积分账号ID
        model.addAttribute("member_id",memberRecord1.getMemberId());
        // 本次积分
        model.addAttribute("member_current_points",memberRecord1.getReceivedPoints());
        //累计积分
        model.addAttribute("member_points",memberRecord1.getReceivedPoints());
        // 流水号----√
        model.addAttribute("shoppingNum",shoppingNum);

        return "receipt";
    }
}

