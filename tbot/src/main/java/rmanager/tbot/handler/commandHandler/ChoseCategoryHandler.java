package rmanager.tbot.handler.commandHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.repository.filter.ProductFilter;
import rmanager.commons.service.ProductService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;
import rmanager.tbot.other.WaiterConst;
import rmanager.tbot.service.CommandService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChoseCategoryHandler implements CommandHandler{

    private CommandService commandService;
    private MessageFactory messageFactory;
    private ProductService productService;

    @Autowired
    public ChoseCategoryHandler(CommandService commandService,
                                MessageFactory messageFactory,
                                ProductService productService) {
        this.commandService = commandService;
        this.messageFactory = messageFactory;
        this.productService = productService;
    }

    @Override
    public List<SendMessage> handle(Command command, TelegramUser telegramUser) {
        List<SendMessage> sendMessageList = new ArrayList<>();

        Integer categoryId = Integer.parseInt(command.getCf());
        ProductFilter productFilter = new ProductFilter();
        productFilter.setProductCategoryId(categoryId);
        List<Product> productList = productService.getByFilter(productFilter);
        for (Product product : productList) {
            SendMessage productMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), product.getName());
            Map<String, String> map = new HashMap<>();
            map.put(WaiterConst.ORDER, commandService.getJsonCommand(CommandType.O_PROD, EntityType.PROD_ID, product.getProductId().toString()));
            productMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
            sendMessageList.add(productMessage);
        }
        return sendMessageList;
    }

}