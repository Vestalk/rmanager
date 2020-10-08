package rmanager.tbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rmanager.commons.entity.ProductCategory;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.service.OrderService;
import rmanager.commons.service.ProductCategoryService;
import rmanager.commons.service.ProductService;
import rmanager.commons.service.TelegramUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SendMessageFactory {

    private OrderService orderService;
    private ProductService productService;
    private TelegramUserService telegramUserService;
    private ProductCategoryService productCategoryService;

    @Autowired
    public SendMessageFactory(OrderService orderService,
                              ProductService productService,
                              TelegramUserService telegramUserService,
                              ProductCategoryService productCategoryService) {
        this.orderService = orderService;
        this.productService = productService;
        this.telegramUserService = telegramUserService;
        this.productCategoryService = productCategoryService;
    }

    public SendMessage createMessage(TelegramUser telegramUser, String text) throws TelegramApiException  {
        ReplyKeyboardMarkup keyboardMarkup;
        String responseText = "";
        telegramUser = changeUserMenuStatus(telegramUser, text);
        if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.START)) {
            responseText = WaiterConst.MAIN_MENU;
            keyboardMarkup = getKeyboard(MenuBar.START_MENU, false);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CATEGORY_LIST)) {
            responseText = WaiterConst.CHOSE_CATEGORY;
            List<ProductCategory> productCategoryList = productCategoryService.getAll();
            List<String> productCategoryNameList = productCategoryList.stream().map(ProductCategory::getName).collect(Collectors.toList());
            keyboardMarkup = getKeyboard(productCategoryNameList, true);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.PRODUCT_LIST)) {
            //TODO
            ProductCategory category = productCategoryService.getByName(text);
            ProductFilter filter = ProductFilter.builder().productCategoryId(category.getProductCategoryId()).build();
            List<String> productNameList = productService.getByFilter(filter).stream().map(product -> product.getName()).collect(Collectors.toList());
            responseText = productNameList.toString();
            keyboardMarkup = getPreviousButton();
        }
        else {
            keyboardMarkup = getKeyboard(MenuBar.START_MENU, false);
        }
        return createMessageWithKeyboard(telegramUser.getTelegramBotChatId(), responseText, keyboardMarkup);
    }

    private TelegramUser changeUserMenuStatus(TelegramUser telegramUser, String text) {
        if (text.equals(WaiterConst.CREATE_ORDER)) {
            telegramUser.setUserMenuStatus(UserMenuStatus.CATEGORY_LIST);
        }
        else if (telegramUser.getUserMenuStatus().equals(UserMenuStatus.CATEGORY_LIST) && !text.equals(WaiterConst.PREVIOUS)) {
            ProductCategory category = productCategoryService.getByName(text);
            if (category != null) {
                telegramUser.setLastMessage(text);
                telegramUser.setUserMenuStatus(UserMenuStatus.PRODUCT_LIST);
            }
        }
        else if (text.equals(WaiterConst.PREVIOUS)) {
            int ordinal = telegramUser.getUserMenuStatus().ordinal();
            if (ordinal > 0) {
                telegramUser.setUserMenuStatus(UserMenuStatus.values()[ordinal - 1]);
            }
        }
        return telegramUserService.save(telegramUser);
    }


    private SendMessage createMessage(Long chatId, String message) throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboardMarkup.setKeyboard(keyboard);
        return new SendMessage().setChatId(chatId).setText(message).setReplyMarkup(keyboardMarkup);
    }

    private SendMessage createMessageWithKeyboard(Long chatId, String message, ReplyKeyboardMarkup keyboardMarkup ) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage().setChatId(chatId).setText(message);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    private ReplyKeyboardMarkup getKeyboard(List<String> buttons, Boolean withPreviousButton) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (int buttonIndex = 0; buttonIndex < buttons.size(); buttonIndex++) {
            row.add(buttons.get(buttonIndex));
            if (buttonIndex == buttons.size() - 1) {
                keyboard.add(row);
                if (withPreviousButton) {
                    row = new KeyboardRow();
                    row.add(WaiterConst.PREVIOUS);
                    keyboard.add(row);
                }
            } else if (buttonIndex >=4) {
                buttonIndex = 0;
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup getPreviousButton() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(WaiterConst.PREVIOUS);
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

}
