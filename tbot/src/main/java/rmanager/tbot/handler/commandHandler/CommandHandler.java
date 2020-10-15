package rmanager.tbot.handler.commandHandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import rmanager.commons.entity.TelegramUser;
import rmanager.tbot.entity.Command;

import java.util.List;

public interface CommandHandler {

    List<SendMessage> handle(Command command, TelegramUser telegramUser);

}
