package rmanager.tbot.handler.commandHandler;

import rmanager.commons.entity.TelegramUser;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.ResponseMessageGroup;

import java.util.List;

public interface CommandHandler {

    List<ResponseMessageGroup> handle(Command command, TelegramUser telegramUser);

}
