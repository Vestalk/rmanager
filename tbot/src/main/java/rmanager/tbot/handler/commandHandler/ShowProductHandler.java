package rmanager.tbot.handler.commandHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import rmanager.commons.entity.Product;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.service.ProductService;
import rmanager.tbot.MessageFactory;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.ResponseMessageGroup;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;
import rmanager.tbot.other.WaiterConst;
import rmanager.tbot.service.CommandService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Component
public class ShowProductHandler implements CommandHandler{

    private CommandService commandService;
    private MessageFactory messageFactory;
    private ProductService productService;

    @Autowired
    public ShowProductHandler(CommandService commandService,
                              MessageFactory messageFactory,
                              ProductService productService) {
        this.commandService = commandService;
        this.messageFactory = messageFactory;
        this.productService = productService;
    }

    @Override
    public List<ResponseMessageGroup> handle(Command command, TelegramUser telegramUser) {
        Integer productId = Integer.parseInt(command.getCf());
        Product product = productService.getById(productId);
        ResponseMessageGroup group = ResponseMessageGroup
                .builder()
                .sendPhotoList(createPhotoMessage(telegramUser, product))
                .sendMessageList(createProductMessage(telegramUser, product))
                .build();
        return Arrays.asList(group);
    }

    private List<SendPhoto> createPhotoMessage(TelegramUser telegramUser, Product product) {
        SendPhoto sendPhoto = null;
        try {
            sendPhoto = new SendPhoto().setChatId(telegramUser.getTelegramBotChatId()).setPhoto("SomeText", new FileInputStream(new File(product.getImg().getImageLink())));
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        return Arrays.asList(sendPhoto);
    }

    private List<SendMessage> createProductMessage(TelegramUser telegramUser, Product product) {
        String productDescriptionMessage = product.getName() + " (" + product.getCost() + " грн)" + "\n" + product.getDescription();
        SendMessage productMessage = messageFactory.createMessage(telegramUser.getTelegramBotChatId(), productDescriptionMessage);
        Map<String, String> map = new HashMap<>();
        map.put(WaiterConst.ORDER, commandService.getJsonCommand(CommandType.O_PROD, EntityType.PROD_ID, product.getProductId().toString()));
        productMessage.setReplyMarkup(messageFactory.createInlineKeyboardMarkup(map));
        return Arrays.asList(productMessage);
    }
}
