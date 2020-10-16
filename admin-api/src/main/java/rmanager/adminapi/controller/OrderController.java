package rmanager.adminapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmanager.adminapi.annotation.AuthRequired;
import rmanager.adminapi.dto.OrderDTO;
import rmanager.adminapi.service.ConvertService;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.UserRole;
import rmanager.commons.service.OrderItemService;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductService;
import rmanager.commons.service.TelegramUserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private OrderService orderService;
    private ConvertService convertService;
    private ProductService productService;
    private OrderItemService orderItemService;
    private TelegramUserService telegramUserService;

    @Autowired
    public OrderController(OrderService orderService,
                           ConvertService convertService,
                           ProductService productService,
                           OrderItemService orderItemService,
                           TelegramUserService telegramUserService) {
        this.orderService = orderService;
        this.convertService = convertService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.telegramUserService = telegramUserService;
    }

    @GetMapping
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity getOrdersInProgress(@RequestHeader(name = "Authentication") String token) {
        List<Order> orderList = orderService.getOrders(Arrays.asList(OrderStatus.IN_PROGRESS, OrderStatus.CREATED));
        List<OrderDTO> orderDTOList = orderList.stream().map(order -> convertService.convertOrderToDTO(order)).collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOList);
    }
}
