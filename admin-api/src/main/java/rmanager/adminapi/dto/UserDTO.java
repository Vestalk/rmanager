package rmanager.adminapi.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer userId;
    private String login;
    private String password;
    private String userRole;
}
