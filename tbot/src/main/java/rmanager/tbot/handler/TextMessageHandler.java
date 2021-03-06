package rmanager.tbot.handler;

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
import rmanager.tbot.entity.ResponseMessageGroup;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;
import rmanager.tbot.other.EmojiConst;
import rmanager.tbot.other.MenuBar;
import rmanager.tbot.other.WaiterConst;
import rmanager.tbot.service.CommandService;

import java.util.*;

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

    public List<ResponseMessageGroup> handleMessage(TelegramUser telegramUser, String text) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        ReplyKeyboardMarkup keyboardMarkup;
        String responseText = "";
        telegramUser = changeUserMenuStatus(telegramUser, text);
        if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.START)) {
            responseText = WaiterConst.MAIN_MENU;
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
        } else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.ORDER)) {
            SendMessage choseCategoryMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), WaiterConst.CHOSE_CATEGORY);
            List<ProductCategory> categoryList = productCategoryService.getAllAvailable();
            Map<String, String> map = new HashMap<>();
            for (ProductCategory category : categoryList) {
                map.put(category.getName(), commandService.getCommandAsJson(CommandType.C_CAT, EntityType.CAT_ID, category.getProductCategoryId().toString()));
            }
            choseCategoryMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
            sendMessageList.add(choseCategoryMessage);

            responseText = EmojiConst.DROOLING_FACE;
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
                for (OrderItem item: order.getOrderItems()) {
                    String messageText = item.getProduct().getName() + " X " + item.getNumber();
                    SendMessage deleteItemMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), messageText);

                    Map<String, String> map = new HashMap<>();
                    map.put(EmojiConst.WASTEBASKET, commandService.getCommandAsJson(CommandType.D_ITEM, EntityType.IT_ID, item.getItemId().toString()));
                    map.put(EmojiConst.PLUS, commandService.getCommandAsJson(CommandType.P_ITEM, EntityType.IT_ID, item.getItemId().toString()));
                    map.put(EmojiConst.MINUS, commandService.getCommandAsJson(CommandType.M_ITEM, EntityType.IT_ID, item.getItemId().toString()));

                    deleteItemMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
                    sendMessageList.add(deleteItemMessage);
                }
                ResponseMessageGroup messageGroup = ResponseMessageGroup.builder().sendMessageList(sendMessageList).build();
                return Arrays.asList(messageGroup);
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
                order.setOrderStatus(OrderStatus.IN_PROGRESS);
                order.setDateCreate(new Date());
                orderService.save(order);

                telegramUser.setUserMenuStatus(UserMenuStatus.START);
                telegramUserService.save(telegramUser);

                String orderCreatedResponseText = WaiterConst.ORDER_CREATED + EmojiConst.DROOLING_FACE;
                SendMessage orderCreatedResponseMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), orderCreatedResponseText);
                sendMessageList.add(orderCreatedResponseMessage);

                responseText = WaiterConst.MAIN_MENU;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.CREATE_ORDER_MENU, false);
            }
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART)) {
            Order order;
            List<Order> orderList = orderService.getOrders(telegramUser.getUserId(), OrderStatus.CREATING);
            if (orderList.isEmpty() || orderList.get(0).getOrderItems().isEmpty()) {
                responseText = WaiterConst.CARD_EMPTY;
                keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
            } else {
                order = orderList.get(0);
                responseText = EmojiConst.CARD + "\n";
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

        ResponseMessageGroup messageGroup = ResponseMessageGroup.builder().sendMessageList(sendMessageList).build();
        return Arrays.asList(messageGroup);
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
