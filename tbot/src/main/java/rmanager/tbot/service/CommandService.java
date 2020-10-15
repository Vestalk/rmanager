package rmanager.tbot.service;

import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;

public interface CommandService {

    String getJsonCommand(CommandType commandType, EntityType entityType, String commandField);
    Command getCommandFromJson(String json);

}
