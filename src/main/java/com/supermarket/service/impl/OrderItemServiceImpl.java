package com.supermarket.service.impl;

import com.supermarket.dao.CommodityDao;
import com.supermarket.dao.OrderDao;
import com.supermarket.dao.OrderItemDao;
import com.supermarket.pojo.Commodity;
import com.supermarket.pojo.OrderItem;
import com.supermarket.service.OrderItemService;
import com.supermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("orderItemService")
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    CommodityDao commodityDao;

    @Override
    public int addBoughtOrderItem(String shoppingNumStr, OrderItem orderItem) {
        //判断商品ID是否重复,重复更新否则插入
        OrderItem commodity = orderItemDao.getOrderCommodityID(shoppingNumStr, orderItem.getCommodity_id());
        //查询库存表中的数据
        Commodity Stock = commodityDao.getCommodityID(orderItem.getCommodity_id());
        //如果查到相同的值,购物车表中的数据进行累加
        if (commodity != null) {
            int count = commodity.getCount() + orderItem.getCount();
            BigDecimal total = commodity.getTotal().add(orderItem.getTotal());
            int id = commodity.getId();
            // 如果购物车count（数量）> 仓库的Stock（数量），不能更新
            if (count > commodity.getStock()) {
                return 0;
            }
            // 更新购物车表，主要是改count（数量）、total（总价）
            orderItemDao.updateOrderItem(id, count, total);
        }
        // 如果不ID不相同相同，并且购物的数量<仓库库存；直接插入
        else {
            // count：本次购物的数量
            // Stock.getStock():查到仓库的库存数量
            int count = orderItem.getCount();
            // 购物的数量>仓库的库存,不可执行插入
            if (count > Stock.getStock()) {
                return 0;
            }
            // 符合以上条件才可插入
            orderItemDao.insertOrderItem(orderItem);
        }
        return 1;
    }

    // 查询购物车表中未结账的数据
    @Override
    public List<OrderItem> getOrderChecked(String shoppingNum) {
        return orderItemDao.getOrderChecked(shoppingNum);
    }

    @Override
    // 现金结账-更新购物车表数量，顺便把结账状态改为1
    public int updateOrderStock(int id, int count) {
        return orderItemDao.updateOrderStock(id, count);
    }

    @Override
    //查询购物车结账的数据
    public List<OrderItem> getOrderYChecked(String shoppingNum) {
        return orderItemDao.getOrderYChecked(shoppingNum);
    }
}