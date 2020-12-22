package com.supermarket.service;

import com.supermarket.pojo.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderItemService {

    // 插入商品到购物车中
    int addBoughtOrderItem(String shoppingNumStr, OrderItem orderItem);

    //根据流水号查询购物车表中未结账的数据-显示到收银管理界面中，等待结账
    List<OrderItem> getOrderChecked(String shoppingNum);

    // 现金结账-更新购物车表数量，顺便把结账状态改为1
    int updateOrderStock(int id,int count);

    //查询购物车结账数据
    List<OrderItem> getOrderYChecked(String shoppingNum);
}
