package rmanager.tbot.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import rmanager.commons.entity.TelegramUser;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.ResponseMessageGroup;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.handler.commandHandler.*;
import rmanager.tbot.service.CommandService;

import java.util.*;

@Component
public class CallbackQueryHandler {

    private MessageFactory messageFactory;
    private CommandService commandService;
    private ShowProductHandler showProductHandler;
    private OrderProductHandler orderProductHandler;
    private ChoseCategoryHandler choseCategoryHandler;
    private DeleteOrderItemHandler deleteOrderItemHandler;
    private PlusMinusOrderItemHandler plusMinusOrderItemHandler;

    @Autowired
    public CallbackQueryHandler(MessageFactory messageFactory,
                                CommandService commandService,
                                ShowProductHandler showProductHandler,
                                OrderProductHandler orderProductHandler,
                                ChoseCategoryHandler choseCategoryHandler,
                                DeleteOrderItemHandler deleteOrderItemHandler,
                                PlusMinusOrderItemHandler plusMinusOrderItemHandler) {
        this.messageFactory = messageFactory;
        this.commandService = commandService;
        this.showProductHandler = showProductHandler;
        this.orderProductHandler = orderProductHandler;
        this.choseCategoryHandler = choseCategoryHandler;
        this.deleteOrderItemHandler = deleteOrderItemHandler;
        this.plusMinusOrderItemHandler = plusMinusOrderItemHandler;
    }

    public List<ResponseMessageGroup> handleMessage(TelegramUser telegramUser, CallbackQuery callbackQuery) {
        Command command = commandService.getCommandFromJson(callbackQuery.getData());
        if (command.getCt().equals(CommandType.C_CAT)) {
            return choseCategoryHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.O_PROD)) {
            return orderProductHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.D_ITEM)) {
            return deleteOrderItemHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.P_ITEM) || command.getCt().equals(CommandType.M_ITEM)) {
            return plusMinusOrderItemHandler.handle(command, telegramUser);
        }
        else if (command.getCt().equals(CommandType.S_PROD)) {
            return showProductHandler.handle(command, telegramUser);
        }
        else {
            List<SendMessage> messageList = Arrays.asList(messageFactory.createMessage(telegramUser.getTelegramBotChatId(), "err"));
            ResponseMessageGroup messageGroup = ResponseMessageGroup.builder().sendMessageList(messageList).build();
            List<ResponseMessageGroup> responseMessageGroupList = Arrays.asList(messageGroup);
            return responseMessageGroupList;
        }
    }
}
