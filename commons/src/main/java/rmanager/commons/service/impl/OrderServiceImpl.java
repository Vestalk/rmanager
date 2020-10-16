package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.repository.OrderRepository;
import rmanager.commons.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getOrders(OrderStatus orderStatus) {
        return orderRepository.getOrders(orderStatus.name());
    }

    @Override
    public List<Order> getOrders(List<OrderStatus> orderStatus) {
        return orderRepository.getOrders(orderStatus.stream().map(Enum::name).collect(Collectors.toList()));
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
