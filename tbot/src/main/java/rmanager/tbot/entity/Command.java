package rmanager.tbot.entity;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Command {

    private CommandType ct;
    private EntityType et;
    private String cf;

}
