package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private Integer bonusBalance;

}
