package rmanager.adminapi.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    private Gson gson;
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

        gson = new Gson();
    }

    @GetMapping
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity getOrdersInProgress(@RequestHeader(name = "Authentication") String token,
                                              @RequestParam(name = "orderStatusList") String orderStatusListStr) {
        OrderStatus[] orderStatusArr = gson.fromJson(orderStatusListStr, OrderStatus[].class);
        List<OrderStatus> orderStatusList = Arrays.asList(orderStatusArr);
        List<Order> orderList = orderService.getOrders(orderStatusList);
        List<OrderDTO> orderDTOList = orderList.stream().map(order -> convertService.convertOrderToDTO(order)).collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOList);
    }

    @GetMapping("/change_status/{orderId}/{orderStatus}")
    @AuthRequired({UserRole.ADMIN})
    public ResponseEntity changeStatus(@RequestHeader(name = "Authentication") String token,
                                       @PathVariable(name = "orderId") Long orderId,
                                       @PathVariable(name = "orderStatus") OrderStatus orderStatus) {
        Order order = orderService.getById(orderId);
        if (order == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        order.setOrderStatus(orderStatus);
        if (orderStatus.equals(OrderStatus.CREATED)) order.setDateCooking(new Date());
        if (orderStatus.equals(OrderStatus.ACCEPTED)) order.setDateExecute(new Date());
        order = orderService.save(order);
        return ResponseEntity.ok(convertService.convertOrderToDTO(order));
    }


}
