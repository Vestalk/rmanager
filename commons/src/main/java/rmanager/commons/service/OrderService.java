package rmanager.commons.service;

import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;

import java.util.List;

public interface OrderService {

    List<Order> getOrders(Long userId, OrderStatus orderStatus);

    Order save(Order order);

}
