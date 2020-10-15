package rmanager.tbot.service;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.CommandType;
import rmanager.tbot.entity.EntityType;

@Service
public class CommandServiceImpl implements CommandService {

    private Gson gson;

    @Autowired
    public CommandServiceImpl() {
        gson = new Gson();
    }

    @Override
    public String getJsonCommand(CommandType commandType, EntityType entityType, String commandField) {
        return gson.toJson(Command.builder()
                .ct(commandType)
                .et(entityType)
                .cf(commandField)
                .build());
    }

    @Override
    public Command getCommandFromJson(String json) {
        return gson.fromJson(json, Command.class);
    }
}
