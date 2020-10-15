package rmanager.tbot.handler.commandHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.service.OrderItemService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;
import rmanager.tbot.other.EmojiConst;
import rmanager.tbot.service.CommandService;

import java.util.*;

@Component
public class PlusMinusOrderItemHandler implements CommandHandler{

    private MessageFactory messageFactory;
    private CommandService commandService;
    private OrderItemService orderItemService;

    @Autowired
    public PlusMinusOrderItemHandler(MessageFactory messageFactory, CommandService commandService, OrderItemService orderItemService) {
        this.messageFactory = messageFactory;
        this.commandService = commandService;
        this.orderItemService = orderItemService;
    }

    @Override
    public List<SendMessage> handle(Command command, TelegramUser telegramUser) {
        OrderItem orderItem = orderItemService.getById(Long.parseLong(command.getCf()));
        if (orderItem == null) return new ArrayList<>();

        if (command.getCt().equals(CommandType.P_ITEM)) {
            Integer number = orderItem.getNumber() + 1;
            orderItem.setNumber(number);
            orderItem = orderItemService.save(orderItem);
        }

        if (command.getCt().equals(CommandType.M_ITEM)) {
            Integer number = orderItem.getNumber() - 1;
            if (number <= 0) {
                orderItemService.delete(orderItem);
                return new ArrayList<>();
            } else {
                orderItem.setNumber(number);
                orderItem = orderItemService.save(orderItem);
            }
        }

        String messageText = orderItem.getProduct().getName() + " X " + orderItem.getNumber();
        SendMessage deleteItemMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), messageText);

        Map<String, String> map = new HashMap<>();
        map.put(EmojiConst.WASTEBASKET, commandService.getJsonCommand(CommandType.D_ITEM, EntityType.IT_ID, orderItem.getItemId().toString()));
        map.put(EmojiConst.PLUS, commandService.getJsonCommand(CommandType.P_ITEM, EntityType.IT_ID, orderItem.getItemId().toString()));
        map.put(EmojiConst.MINUS, commandService.getJsonCommand(CommandType.M_ITEM, EntityType.IT_ID, orderItem.getItemId().toString()));

        deleteItemMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
        return Arrays.asList(deleteItemMessage);
    }
}
