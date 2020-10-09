package rmanager.tbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
@PropertySource("classpath:telegrambot.properties")
public class BotConfiguration {

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    //http://t.me/testobotor_bot
    @Bean
    public WaiterTelegramBot waiterTelegramBot(MessageHandler messageHandler) {
        return new WaiterTelegramBot(new DefaultBotOptions(), botName, botToken, messageHandler);
    }

}
