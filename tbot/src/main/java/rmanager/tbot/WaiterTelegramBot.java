package rmanager.tbot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rmanager.tbot.entity.ResponseMessageGroup;

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
        List<ResponseMessageGroup> messageGroupList = messageHandler.handleMessage(update);
        if (!messageGroupList.isEmpty()) {
            messageGroupList.forEach(group -> {
                try {
                    if (group.getSendPhotoList() != null && !group.getSendPhotoList().isEmpty()) {
                        for (SendPhoto photo: group.getSendPhotoList()) {
                            execute(photo);
                        }
                    }
                    if (group.getSendMessageList() != null && !group.getSendMessageList().isEmpty()) {
                        for (SendMessage message: group.getSendMessageList()) {
                            execute(message);
                        }
                    }
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
