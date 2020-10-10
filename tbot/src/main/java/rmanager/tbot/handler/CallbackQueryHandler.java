package rmanager.tbot.handler;

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
import rmanager.commons.service.TelegramUserService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.other.CallbackQueryConst;
import rmanager.tbot.other.MenuBar;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CallbackQueryHandler {

    private OrderService orderService;
    private MessageFactory messageFactory;
    private ProductService productService;
    private OrderItemService orderItemService;

    @Autowired
    public CallbackQueryHandler(OrderService orderService,
                          MessageFactory messageFactory,
                          ProductService productService,
                          OrderItemService orderItemService) {
        this.orderService = orderService;
        this.messageFactory = messageFactory;
        this.productService = productService;
        this.orderItemService = orderItemService;
    }

    public List<SendMessage> handleMessage(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        Message message = callbackQuery.getMessage();
        Long chatId = message.getChatId();
        String callbackData = callbackQuery.getData();

        String responseText = "-";
        ReplyKeyboardMarkup keyboardMarkup = null;
        if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.ORDER) && callbackData.contains(CallbackQueryConst.CATEGORY_ID)) {
            Integer categoryId = Integer.parseInt(callbackData.replace(CallbackQueryConst.CATEGORY_ID, ""));

            ProductFilter productFilter = new ProductFilter();
            productFilter.setProductCategoryId(categoryId);
            List<Product> productList = productService.getByFilter(productFilter);
            for (Product product: productList) {
                SendMessage productMessage = messageFactory.createMessage(chatId, product.getName());
                Map<String, String> map = new HashMap<>();
                map.put("Заказать", CallbackQueryConst.PRODUCT_ID + product.getProductId());
                productMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
                sendMessageList.add(productMessage);
            }

            keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.ORDER) && callbackData.contains(CallbackQueryConst.PRODUCT_ID)) {
            Integer productId = Integer.parseInt(callbackData.replace(CallbackQueryConst.PRODUCT_ID, ""));
            Product product = productService.getById(productId);
            if (product != null){
                createOrderItem(telegramUser, product);
            }
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
