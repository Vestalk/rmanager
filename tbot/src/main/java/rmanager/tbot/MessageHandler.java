package rmanager.tbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import rmanager.commons.entity.*;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.service.*;
import rmanager.tbot.entity.ResponseMessageGroup;
import rmanager.tbot.handler.CallbackQueryHandler;
import rmanager.tbot.handler.TextMessageHandler;

import java.util.*;

@Component
public class MessageHandler {

    private TextMessageHandler textMessageHandler;
    private TelegramUserService telegramUserService;
    private CallbackQueryHandler callbackQueryHandler;

    @Autowired
    public MessageHandler(TextMessageHandler textMessageHandler,
                          TelegramUserService telegramUserService,
                          CallbackQueryHandler callbackQueryHandler) {
        this.textMessageHandler = textMessageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.telegramUserService = telegramUserService;
    }

    public List<ResponseMessageGroup> handleMessage(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Chat chat = message.getChat();
            TelegramUser telegramUser = telegramUserService.getByTelegramBotChatId(chat.getId());
            if (telegramUser == null) {
                telegramUser = saveNewUser(chat);
            }
            return textMessageHandler.handleMessage(telegramUser, message.getText());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Message message = callbackQuery.getMessage();
            Chat chat = message.getChat();
            TelegramUser telegramUser = telegramUserService.getByTelegramBotChatId(chat.getId());
            if (telegramUser == null) {
                telegramUser = saveNewUser(chat);
                return textMessageHandler.handleMessage(telegramUser, message.getText());
            }
            return callbackQueryHandler.handleMessage(telegramUser, callbackQuery);
        }
        return null;
    }

    private TelegramUser saveNewUser(Chat chat) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setFirstName(chat.getFirstName());
        telegramUser.setLastName(chat.getLastName());
        telegramUser.setUserName(chat.getUserName());
        telegramUser.setTelegramBotChatId(chat.getId());
        telegramUser.setUserMenuStatus(UserMenuStatus.START);
        return telegramUserService.save(telegramUser);
    }

}
