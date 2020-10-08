package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.commons.entity.TelegramUser;
import rmanager.commons.repository.TelegramUserRepository;
import rmanager.commons.service.TelegramUserService;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    protected TelegramUserRepository telegramUserRepository;

    @Autowired
    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public TelegramUser getByUserId(Long userId) {
        return telegramUserRepository.findById(userId).orElse(null);
    }

    @Override
    public TelegramUser getByTelegramBotChatId(Long telegramBotChatId) {
        return telegramUserRepository.findByTelegramBotChatId(telegramBotChatId).orElse(null);
    }

    @Override
    public TelegramUser save(TelegramUser user) {
        return telegramUserRepository.saveAndFlush(user);
    }

    @Override
    public void delete(TelegramUser user) {
        telegramUserRepository.delete(user);
    }
}
