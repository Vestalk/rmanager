package rmanager.tbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.TelegramUserService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.other.CallbackQueryConst;
import rmanager.tbot.other.MenuBar;
import rmanager.tbot.other.WaiterConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TextMessageHandler {

    private MessageFactory messageFactory;
    private TelegramUserService telegramUserService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public TextMessageHandler(MessageFactory messageFactory, TelegramUserService telegramUserService, ProductCategoryService productCategoryService) {
        this.messageFactory = messageFactory;
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
            List<ProductCategory> categoryList = productCategoryService.getAll();
            Map<String, String> map = new HashMap<>();
            for (ProductCategory category : categoryList) {
                map.put(category.getName(), CallbackQueryConst.CATEGORY_ID + category.getProductCategoryId());
            }
            choseCategoryMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
            sendMessageList.add(choseCategoryMessage);

            responseText = WaiterConst.CHOSE_CATEGORY;
            keyboardMarkup = messageFactory.getKeyboard(MenuBar.SHOW_CARD_MENU, true);
        } else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CART)) {
            //TODO
            responseText = WaiterConst.CART;
            keyboardMarkup = messageFactory.getPreviousButton();
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
        } else {
            telegramUser.setUserMenuStatus(UserMenuStatus.START);
        }
        return telegramUserService.save(telegramUser);
    }

}
