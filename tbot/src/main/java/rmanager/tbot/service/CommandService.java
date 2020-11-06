package rmanager.tbot.service;

import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;

public interface CommandService {

    String getCommandAsJson(CommandType commandType, EntityType entityType, String commandField);
    Command convertCommandFromJson(String json);

}
