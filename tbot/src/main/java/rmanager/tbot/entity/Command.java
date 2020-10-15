package rmanager.tbot.entity;

import lombok.*;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;

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
