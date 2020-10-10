package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.repository.OrderRepository;
import rmanager.commons.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders(Long userId, OrderStatus orderStatus) {
        return orderRepository.getOrders(userId, orderStatus.name());
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

}
