package rmanager.commons.service;

import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;

import java.util.List;

public interface OrderService {

    Order getById(Long id);

    List<Order> getOrders(OrderStatus orderStatus);
    List<Order> getOrders(List<OrderStatus> orderStatus);
    List<Order> getOrders(Long userId, OrderStatus orderStatus);

    Order save(Order order);

}
