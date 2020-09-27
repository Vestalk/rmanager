package rmanager.commons.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "telegram_users")
public class TelegramUser {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "telegram_bot_chat_id")
    private Long telegramBotChatId;

    @Column(name = "bonus_balance")
    private Integer bonusBalance;

}
