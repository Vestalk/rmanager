package rmanager.commons.service;

import rmanager.commons.entity.OrderItem;

public interface OrderItemService {

    OrderItem getById(Long itemId);

    OrderItem save(OrderItem orderItem);
    void delete(OrderItem orderItem);

}
