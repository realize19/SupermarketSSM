package com.supermarket.controller;

import com.alibaba.fastjson.JSON;
import com.supermarket.pojo.Commodity;
import com.supermarket.pojo.IDUtil;
import com.supermarket.pojo.OrderItem;
import com.supermarket.service.CommodityService;
import com.supermarket.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/commodity")
public class CommodityController {
    @Autowired
    @Qualifier("commodityService")
    private CommodityService commodityService;

    @Autowired
    private OrderItemService orderItemService;
    //进货管理查询所有数据
    @RequestMapping(value ="/getCommodity")
    public String getCommodities(Model model){
         List<Commodity> list = commodityService.getCommodities();
        model.addAttribute("commodity",list);
        return "commodity";
    }

    //进货功能
    @RequestMapping(value ="/inputCommodities")
    private String inputCommodities(Model model,Commodity commodity){
        String forwardPage;
        //进货管理添加：1.相同商品条码（更新仓库表走update）2.不同商品条码（直接添加走insert）
        boolean bl = commodityService.inputCommodity(commodity);
        if (bl == false){
            forwardPage = "commodity";
        }
        else{
            forwardPage = "commodity";
        }
        //刷新仓库管理界面
        List<Commodity> list = commodityService.getCommodities();
        model.addAttribute("commodity",list);
        return forwardPage;
    }

    // 收银-输入商品条码和数量进行查询
    @RequestMapping(value = "addBoughtCommodity")
    private String addBoughtCommodity(Model model, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forwardPage="cashier";
        // 商品ID--数据库是int类型的需要强转为String
        int commodityID = Integer.parseInt(req.getParameter("commodityID").toString());
        // 流水号
        String  shoppingNumStr = req.getParameter("shoppingNum");
        // 输入的数量
        int count = Integer.parseInt(req.getParameter("count"));
        // 给总价格赋值初始值为0
        BigDecimal totalCost = new BigDecimal(0);
        // 共有几件商品初始化为0
        int category = 0;
        // 判断商品条码是否生成，为空生成
        if (shoppingNumStr == null || "".equals( shoppingNumStr)){
            shoppingNumStr = IDUtil.getId().toString();
        }
        req.setAttribute("shoppingNum", shoppingNumStr);

        // 将输入的商品条码带进去查询，查到后插入到购物车表中。
        Commodity commodity = commodityService.getCommodityID(commodityID);
        // 如果没有这个库存编号，报错
        if (commodity == null) {
            forwardPage = "error";
            RequestDispatcher view = req.getRequestDispatcher(forwardPage);
            view.forward(req,resp);
        }
        // 传值定义orderIter接收（有====说明是从commodit表中获取到的）
        OrderItem orderItem=new OrderItem();
        //小票流水号ID数据库设置为自增加，这里不传值
        //流水号
        orderItem.setOrder_number(shoppingNumStr);
        //商品条码     ====
        orderItem.setCommodity_id(commodityID);
        //商品名称     ====
        orderItem.setCommodity_name(commodity.getName());
        //单价         ====
        orderItem.setPrice(commodity.getPrice());
        //数量
        orderItem.setCount(count);
        //总价
        BigDecimal number ;
        int value = count;
        number = BigDecimal.valueOf(value);
        orderItem.setTotal(number.multiply(commodity.getPrice()));
        //结账状态
        orderItem.setIsChecked(0);
        //规格等级      ====
        orderItem.setSpecification(commodity.getSpecification());
        //单位         ====
        orderItem.setUnits(commodity.getUnits());
        //当前库存      ====
        orderItem.setStock(commodity.getStock());

        // 将输入的商品条码添加到购物车表中

        int orderItemListFlag = orderItemService.addBoughtOrderItem(shoppingNumStr, orderItem);
        // 判断是否插入成功-如果购物车的数量>仓库的数量，就会返回，进入错误界面
        if (orderItemListFlag == 0){
            forwardPage = "error";
            RequestDispatcher view = req.getRequestDispatcher(forwardPage);
            view.forward(req,resp);
        }else{
            // 根据流水号查询购物车表中未结账的数据-显示到收银管理界面中，等待结账
            List<OrderItem> orderChecked = orderItemService.getOrderChecked(shoppingNumStr);
            // 将金额一列价钱相加
            for (OrderItem item : orderChecked) {
                totalCost = totalCost.add(item.getTotal());
            }
            category = orderChecked.size();
            // 传回来的List集合，购物车表中未结账的数据
            req.setAttribute("orderItemList", orderChecked);
            // 共计多少钱
            req.setAttribute("totalCost", totalCost.doubleValue());
            // 添加一次商品条数增加一次
            req.setAttribute("category", category);
        }
        List<OrderItem> orderChecked = orderItemService.getOrderChecked(shoppingNumStr);
        model.addAttribute("orderItemList",orderChecked);
        return forwardPage;
    }

    // 收银-勾选商品进行删除

    @RequestMapping(value = "/removeBoughtCommodity",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    private String removeBoughtCommodity(String[] ids,String shoppingNum) {
        // 将未结账的字段改为2 2：代表删除
        for (int a = 0; a < ids.length; a++) {
            commodityService.removeBoughtCommodity(ids[a]);
        }
        // 根据流水号查询购物车表中未结账的数据-刷新一下界面
        List<OrderItem> orderChecked = orderItemService.getOrderChecked(shoppingNum);
        String data = JSON.toJSONString(orderChecked);
        return data;
    }
}
