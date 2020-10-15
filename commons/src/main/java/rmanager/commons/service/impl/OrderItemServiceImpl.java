package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.repository.OrderItemRepository;
import rmanager.commons.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem getById(Long itemId) {
        return orderItemRepository.findById(itemId).orElse(null);
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.saveAndFlush(orderItem);
    }

    @Override
    public void delete(OrderItem orderItem) {
        orderItemRepository.delete(orderItem.getItemId());
    }
}
