package rmanager.tbot.handler.commandHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.service.OrderItemService;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.service.CommandService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderProductHandler implements CommandHandler{

    private OrderService orderService;
    private ProductService productService;
    private OrderItemService orderItemService;

    @Autowired
    public OrderProductHandler(OrderService orderService,
                               ProductService productService,
                               OrderItemService orderItemService) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderItemService = orderItemService;
    }

    @Override
    public List<SendMessage> handle(Command command, TelegramUser telegramUser) {
        Integer productId = Integer.parseInt(command.getCf());
        Product product = productService.getById(productId);
        if (product != null) {
            createOrderItem(telegramUser, product);
        }
        return new ArrayList<>();
    }

    private OrderItem createOrderItem(TelegramUser telegramUser, Product product) {
        Order order;
        List<Order> orderList = orderService.getOrders(telegramUser.getUserId(), OrderStatus.CREATING);
        if (orderList.isEmpty()) {
            order = new Order();
            order.setUser(telegramUser);
            order.setOrderStatus(OrderStatus.CREATING);
            order = orderService.save(order);
        } else {
            order = orderList.get(0);
        }

        Set<OrderItem> orderItemSet = order.getOrderItems();
        orderItemSet = orderItemSet.stream().filter(item -> item.getProduct().getProductId().equals(product.getProductId())).collect(Collectors.toSet());

        OrderItem orderItem = orderItemSet.stream().filter(item -> item.getProduct().getProductId().equals(product.getProductId())).findAny().orElse(null);
        if (orderItem != null) {
            Integer number = orderItem.getNumber();
            orderItem.setNumber(number + 1);
        } else {
            orderItem = new OrderItem();
            orderItem.setNumber(1);
            orderItem.setProduct(product);
            orderItem.setOrder(order);
        }
        return orderItemService.save(orderItem);
    }
}
