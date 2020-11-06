package rmanager.tbot.service;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmanager.tbot.entity.Command;
import rmanager.tbot.entity.other.CommandType;
import rmanager.tbot.entity.other.EntityType;

@Service
public class CommandServiceImpl implements CommandService {

    private Gson gson;

    @Autowired
    public CommandServiceImpl() {
        gson = new Gson();
    }

    @Override
    public String getCommandAsJson(CommandType commandType, EntityType entityType, String commandField) {
        return gson.toJson(Command.builder()
                .ct(commandType)
                .et(entityType)
                .cf(commandField)
                .build());
    }

    @Override
    public Command convertCommandFromJson(String json) {
        return gson.fromJson(json, Command.class);
    }
}
