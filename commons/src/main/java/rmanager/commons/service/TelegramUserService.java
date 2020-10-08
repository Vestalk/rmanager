package rmanager.commons.service;

import rmanager.commons.entity.TelegramUser;

public interface TelegramUserService {

    TelegramUser getByUserId(Long userId);
    TelegramUser getByTelegramBotChatId(Long telegramBotChatId);

    TelegramUser save(TelegramUser user);
    void delete(TelegramUser user);

}
