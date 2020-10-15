package rmanager.tbot.handler;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.TelegramUserService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;
import rmanager.tbot.other.MenuBar;
import rmanager.tbot.other.WaiterConst;
import rmanager.tbot.service.CommandService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TextMessageHandler {

    private OrderService orderService;
    private MessageFactory messageFactory;
    private CommandService commandService;
    private TelegramUserService telegramUserService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public TextMessageHandler(OrderService orderService,
                              MessageFactory messageFactory,
                              CommandService commandService,
                              TelegramUserService telegramUserService,
                              ProductCategoryService productCategoryService) {
        this.orderService = orderService;
        this.messageFactory = messageFactory;
        this.commandService = commandService;
        this.telegramUserService = telegramUserService;
        this.productCategoryService = productCategoryService;
    }

    public List<SendMessage> handleMessage(TelegramUser telegramUser, String text) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        ReplyKeyboardMarkup keyboardMarkup;
        String responseText = "";
        telegramUser = changeUserMenuStatus(telegramUser, text);
        if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.START)) {
            responseText = WaiterConst.MAIN_MENU;
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
        } else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.ORDER)) {
            SendMessage choseCategoryMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), "-");
            List<ProductCategory> categoryList = productCategoryService.getAllAvailable();
            Map<String, String> map = new HashMap<>();
            for (ProductCategory category : categoryList) {
                map.put(category.getName(), commandService.getJsonCommand(CommandType.C_CAT, EntityType.CAT_ID, category.getProductCategoryId().toString()));
            }
            choseCategoryMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
            sendMessageList.add(choseCategoryMessage);

            responseText = WaiterConst.CHOSE_CATEGORY;
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART) && text.equals(WaiterConst.EDIT)) {
            Order order;
            List<Order> orderList = orderService.getOrders(telegramUser.getUserId(), OrderStatus.CREATING);
            if (orderList.isEmpty()) {
                responseText = WaiterConst.CARD_EMPTY;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
            } else {
                order = orderList.get(0);
                //TODO
                responseText = "Test";
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
            }
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART) && text.equals(WaiterConst.SAVE_ORDER)) {
            Order order;
            List<Order> orderList = orderService.getOrders(telegramUser.getUserId(), OrderStatus.CREATING);
            if (orderList.isEmpty()) {
                responseText = WaiterConst.CARD_EMPTY;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
            } else {
                order = orderList.get(0);
                order.setOrderStatus(OrderStatus.CREATED);
                orderService.save(order);

                telegramUser.setUserMenuStatus(UserMenuStatus.START);
                telegramUserService.save(telegramUser);

                SendMessage orderCreated = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), "Оформлено");
                sendMessageList.add(orderCreated);

                responseText = WaiterConst.MAIN_MENU;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
            }
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART)) {
            Order order;
            List<Order> orderList = orderService.getOrders(telegramUser.getUserId(), OrderStatus.CREATING);
            if (orderList.isEmpty()) {
                responseText = WaiterConst.CARD_EMPTY;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
            } else {
                order = orderList.get(0);
                for (OrderItem orderItem :order.getOrderItems()){
                    responseText = responseText + orderItem.getProduct().getName() + " X " + orderItem.getNumber() + "\n";
                }
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.CARD_MENU, true);
            }
        } else {
            responseText = "Test";
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
        }
        SendMessage sendMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), responseText);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessageList.add(sendMessage);
        return sendMessageList;
    }

    private TelegramUser changeUserMenuStatus(TelegramUser telegramUser, String text) {
        if (text.equals(WaiterConst.CREATE_ORDER)) {
            telegramUser.setUserMenuStatus(UserMenuStatus.ORDER);
        } else if (text.equals(WaiterConst.SHOW_CART)) {
            telegramUser.setUserMenuStatus(UserMenuStatus.CART);
        } else if (text.equals(WaiterConst.PREVIOUS)) {
            int ordinal = telegramUser.getUserMenuStatus().ordinal();
            if (ordinal > 0) {
                telegramUser.setUserMenuStatus(UserMenuStatus.values()[ordinal - 1]);
            }
        } else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART) && text.equals(WaiterConst.SAVE_ORDER)) {
            return telegramUser;
        } else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART) && text.equals(WaiterConst.EDIT)) {
            return telegramUser;
        } else {
            telegramUser.setUserMenuStatus(UserMenuStatus.START);
        }
        return telegramUserService.save(telegramUser);
    }

}
