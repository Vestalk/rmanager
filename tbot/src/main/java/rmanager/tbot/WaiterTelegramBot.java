package rmanager.tbot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class WaiterTelegramBot extends TelegramLongPollingBot {

    private String botName;
    private String botToken;
    private MessageHandler messageHandler;

    public WaiterTelegramBot(DefaultBotOptions options,
                             String botName,
                             String botToken,
                             MessageHandler messageHandler) {
        super(options);
        this.botName = botName;
        this.botToken = botToken;
        this.messageHandler = messageHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<SendMessage> sendMessageList = messageHandler.handleMessage(update);
        if (!sendMessageList.isEmpty()) {
            sendMessageList.forEach(sendMessage -> {
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        }
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
