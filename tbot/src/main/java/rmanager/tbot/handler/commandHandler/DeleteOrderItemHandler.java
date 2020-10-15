package rmanager.tbot.handler.commandHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rmanager.commons.entity.OrderItem;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.service.OrderItemService;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.ResponseMessageGroup;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteOrderItemHandler implements CommandHandler{

    private OrderItemService orderItemService;

    @Autowired
    public DeleteOrderItemHandler(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @Override
    public List<ResponseMessageGroup> handle(Command command, TelegramUser telegramUser) {
        OrderItem orderItem = orderItemService.getById(Long.parseLong(command.getCf()));
        if (orderItem != null) orderItemService.delete(orderItem);
        return new ArrayList<>();
    }
}
