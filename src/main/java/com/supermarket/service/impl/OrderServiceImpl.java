package com.supermarket.service.impl;

import com.supermarket.dao.OrderDao;
import com.supermarket.pojo.Order;
import com.supermarket.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;


    @Override
    public boolean addOrder(Order order) {
        Order order1 = orderDao.getOrder(order.getId());
        if (order1 != null){
            return  false;
        }else {
            orderDao.insertOrder(order);
            return  true;
        }
    }
}
