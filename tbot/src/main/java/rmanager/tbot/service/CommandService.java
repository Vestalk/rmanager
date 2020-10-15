package rmanager.tbot.service;

import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;

public interface CommandService {

    String getJsonCommand(CommandType commandType, EntityType entityType, String commandField);
    Command getCommandFromJson(String json);

}
