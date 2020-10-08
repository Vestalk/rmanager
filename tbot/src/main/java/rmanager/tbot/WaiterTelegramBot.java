package rmanager.tbot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.service.TelegramUserService;


public class WaiterTelegramBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;
    private SendMessageFactory sendMessageFactory;
    private TelegramUserService telegramUserService;

    public WaiterTelegramBot(DefaultBotOptions options,
                             String botName,
                             String botToken,
                             SendMessageFactory sendMessageFactory,
                             TelegramUserService telegramUserService) {
        super(options);
        this.botName = botName;
        this.botToken = botToken;
        this.sendMessageFactory = sendMessageFactory;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Chat chat = message.getChat();

        TelegramUser telegramUser = telegramUserService.getByTelegramBotChatId(chat.getId());
        if (telegramUser == null) {
            telegramUser = saveNewUser(chat);
        }

        try {
            execute(sendMessageFactory.createMessage(telegramUser, message.getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
