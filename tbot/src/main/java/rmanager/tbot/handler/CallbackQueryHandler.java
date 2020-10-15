package rmanager.tbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import rmanager.commons.entity.TelegramUser;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.handler.commandHandler.ChoseCategoryHandler;
import rmanager.tbot.handler.commandHandler.DeleteOrderItemHandler;
import rmanager.tbot.handler.commandHandler.OrderProductHandler;
import rmanager.tbot.handler.commandHandler.PlusMinusOrderItemHandler;
import rmanager.tbot.service.CommandService;

import java.util.*;

@Component
public class CallbackQueryHandler {

    private MessageFactory messageFactory;
    private CommandService commandService;
    private OrderProductHandler orderProductHandler;
    private ChoseCategoryHandler choseCategoryHandler;
    private DeleteOrderItemHandler deleteOrderItemHandler;
    private PlusMinusOrderItemHandler plusMinusOrderItemHandler;

    @Autowired
    public CallbackQueryHandler(MessageFactory messageFactory,
                                CommandService commandService,
                                OrderProductHandler orderProductHandler,
                                ChoseCategoryHandler choseCategoryHandler,
                                DeleteOrderItemHandler deleteOrderItemHandler,
                                PlusMinusOrderItemHandler plusMinusOrderItemHandler) {
        this.messageFactory = messageFactory;
        this.commandService = commandService;
        this.orderProductHandler = orderProductHandler;
        this.choseCategoryHandler = choseCategoryHandler;
        this.deleteOrderItemHandler = deleteOrderItemHandler;
        this.plusMinusOrderItemHandler = plusMinusOrderItemHandler;
    }

    public List<SendMessage> handleMessage(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        List<SendMessage> sendMessageList = new ArrayList<>();
        Command command = commandService.getCommandFromJson(callbackQuery.getData());
        if (command.getCt().equals(CommandType.C_CAT)) {
            sendMessageList = choseCategoryHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.O_PROD)) {
            sendMessageList = orderProductHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.D_ITEM)) {
            sendMessageList = deleteOrderItemHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.P_ITEM) || command.getCt().equals(CommandType.M_ITEM)) {
            sendMessageList = plusMinusOrderItemHandler.handle(command, telegramUser);
        } else {
            SendMessage sendMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), "err");
            return Arrays.asList(sendMessage);
        }
        return sendMessageList;
    }
}
