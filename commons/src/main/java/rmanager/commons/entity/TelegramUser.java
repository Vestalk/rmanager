package rmanager.commons.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import rmanager.commons.entity.other.UserMenuStatus;
import rmanager.commons.entity.other.UserRole;

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
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "telegram_bot_chat_id", nullable = false)
    private Long telegramBotChatId;

    @Column(name = "user_menu_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserMenuStatus userMenuStatus;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "bonus_balance")
    private Integer bonusBalance;

}
