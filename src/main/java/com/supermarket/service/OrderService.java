package com.supermarket.service;

import com.supermarket.pojo.Order;
import org.springframework.stereotype.Controller;

@Controller
public interface OrderService {
    //现金结账
    boolean addOrder(Order order);
}
