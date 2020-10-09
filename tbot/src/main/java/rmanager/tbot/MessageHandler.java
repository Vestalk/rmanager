package rmanager.tbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.ProductService;
import rmanager.commons.service.TelegramUserService;
import rmanager.tbot.other.MenuBar;
import rmanager.tbot.other.WaiterConst;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MessageHandler {

    private OrderService orderService;
    private MessageFactory messageFactory;
    private ProductService productService;
    private TelegramUserService telegramUserService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public MessageHandler(OrderService orderService,
                          MessageFactory messageFactory,
                          ProductService productService,
                          TelegramUserService telegramUserService,
                          ProductCategoryService productCategoryService) {
        this.orderService = orderService;
        this.messageFactory = messageFactory;
        this.productService = productService;
        this.telegramUserService = telegramUserService;
        this.productCategoryService = productCategoryService;
    }

    public List<SendMessage> handleMessage(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            TelegramUser telegramUser = telegramUserService.getByTelegramBotChatId(chat.getId());
            if (telegramUser == null) {
                telegramUser = saveNewUser(chat);
            }
            return handleMessageText(telegramUser, message.getText());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            Chat chat = message.getChat();
            TelegramUser telegramUser = telegramUserService.getByTelegramBotChatId(chat.getId());
            if (telegramUser == null) {
                telegramUser = saveNewUser(chat);
                return handleMessageText(telegramUser, message.getText());
            }
            return Arrays.asList(new SendMessage().setText(callbackQuery.getData()).setChatId(message.getChatId()));
        }
        return null;
    }

    private List<SendMessage> handleMessageText(TelegramUser telegramUser, String text) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        ReplyKeyboardMarkup keyboardMarkup;
        String responseText = "";
        telegramUser = changeUserMenuStatus(telegramUser, text);
        if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.START)) {
            responseText = WaiterConst.MAIN_MENU;
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.START_MENU, false);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CATEGORY_LIST)) {
            SendMessage choseCategoryMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), "-");
            List<ProductCategory> categoryList = productCategoryService.getAll();
            Map<String, String> map = new HashMap<>();
            for(ProductCategory category: categoryList) {
                map.put(category.getName(), category.getProductCategoryId().toString());
            }
            choseCategoryMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
            sendMessageList.add(choseCategoryMessage);

            responseText = WaiterConst.CHOSE_CATEGORY;
            keyboardMarkup = messageFactory.getPreviousButton();
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.PRODUCT_LIST)) {
            //TODO
            ProductCategory category;
            if (text.equals(WaiterConst.PREVIOUS)) {
                category = productCategoryService.getByName(telegramUser.getLastMessage());
            } else {
                category = productCategoryService.getByName(text);
            }

            ProductFilter filter = ProductFilter.builder().productCategoryId(category.getProductCategoryId()).build();
            List<String> productNameList = productService.getByFilter(filter).stream().map(Product::getName).collect(Collectors.toList());

            responseText = productNameList.toString();
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.PRODUCT_LIST_MENU, true);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART)) {
            //TODO
            responseText = WaiterConst.CART;
            keyboardMarkup = messageFactory.getPreviousButton();
        }
        else {
            responseText = "Test";
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.START_MENU, false);
        }
        SendMessage sendMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), responseText);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessageList.add(sendMessage);
        return sendMessageList;
    }

    private TelegramUser changeUserMenuStatus(TelegramUser telegramUser, String text) {
        if (text.equals(WaiterConst.CREATE_ORDER)) {
            telegramUser.setUserMenuStatus(UserMenuStatus.CATEGORY_LIST);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.PRODUCT_LIST) && text.equals(WaiterConst.SHOW_CART)) {
            telegramUser.setUserMenuStatus(UserMenuStatus.CART);
        }
//        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CATEGORY_LIST) && !text.equals(WaiterConst.PREVIOUS)) {
//            ProductCategory category = productCategoryService.getByName(text);
//            if (category != null) {
//                telegramUser.setLastMessage(text);
//                telegramUser.setUserMenuStatus(UserMenuStatus.PRODUCT_LIST);
//            }
//        }
        else if (text.equals(WaiterConst.PREVIOUS)) {
            int ordinal = telegramUser.getUserMenuStatus().ordinal();
            if (ordinal > 0) {
                telegramUser.setUserMenuStatus(UserMenuStatus.values()[ordinal - 1]);
            }
        }
        else {
            telegramUser.setUserMenuStatus(UserMenuStatus.START);
        }
        return telegramUserService.save(telegramUser);
    }

    private TelegramUser saveNewUser(Chat chat) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setFirstName(chat.getFirstName());
        telegramUser.setLastLame(chat.getLastName());
        telegramUser.setUserName(chat.getUserName());
        telegramUser.setTelegramBotChatId(chat.getId());
        telegramUser.setUserMenuStatus(UserMenuStatus.START);
        return telegramUserService.save(telegramUser);
    }

}
