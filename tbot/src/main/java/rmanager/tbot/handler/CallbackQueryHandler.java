package rmanager.tbot.handler;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.service.OrderItemService;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;
import rmanager.tbot.other.MenuBar;
import rmanager.tbot.service.CommandService;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CallbackQueryHandler {

    private OrderService orderService;
    private MessageFactory messageFactory;
    private ProductService productService;
    private CommandService commandService;
    private OrderItemService orderItemService;

    @Autowired
    public CallbackQueryHandler(OrderService orderService,
                                MessageFactory messageFactory,
                                ProductService productService,
                                CommandService commandService,
                                OrderItemService orderItemService) {
        this.orderService = orderService;
        this.messageFactory = messageFactory;
        this.productService = productService;
        this.commandService = commandService;
        this.orderItemService = orderItemService;
    }

    public List<SendMessage> handleMessage(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();

        Command command = commandService.getCommandFromJson(callbackQuery.getData());

        String responseText = "-";
        ReplyKeyboardMarkup keyboardMarkup = null;
        if (command.getCt().equals(CommandType.C_CAT)) {
            Integer categoryId = Integer.parseInt(command.getCf());

            ProductFilter productFilter = new ProductFilter();
            productFilter.setProductCategoryId(categoryId);
            List<Product> productList = productService.getByFilter(productFilter);
            for (Product product : productList) {
                SendMessage productMessage = messageFactory.createMessage(chatId, product.getName());
                Map<String, String> map = new HashMap<>();
                map.put("Заказать", commandService.getJsonCommand(CommandType.O_PROD, EntityType.PROD_ID, product.getProductId().toString()));
                productMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
                sendMessageList.add(productMessage);
            }

            keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
        }
        else if (command.getCt().equals(CommandType.O_PROD)) {
            Integer productId = Integer.parseInt(command.getCf());
            Product product = productService.getById(productId);
            if (product != null) {
                createOrderItem(telegramUser, product);
            }
            return new ArrayList<>();
        }
        else if (command.getCt().equals(CommandType.D_ITEM)) {
            OrderItem orderItem = orderItemService.getById(Long.parseLong(command.getCf()));
            if (orderItem != null) orderItemService.delete(orderItem);
            return new ArrayList<>();
        }

        SendMessage sendMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), responseText);
        if (keyboardMarkup != null) sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessageList.add(sendMessage);
        return sendMessageList;
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
