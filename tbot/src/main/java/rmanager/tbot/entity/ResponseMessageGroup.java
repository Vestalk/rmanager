package rmanager.tbot.entity;

import lombok.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessageGroup {

    private List<SendMessage> sendMessageList;
    private List<SendPhoto> sendPhotoList;

}
