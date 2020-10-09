package rmanager.tbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import rmanager.tbot.other.WaiterConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MessageFactory {

    public SendMessage createMessage(Long chatId, String message) {
        return new SendMessage().setChatId(chatId).setText(message);
    }

    public ReplyKeyboardMarkup getKeyboard(List<String> buttons, Boolean withPreviousButton) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (int buttonIndex = 0, buttonInRow = 1; buttonIndex < buttons.size(); buttonIndex++, buttonInRow++) {
            row.add(buttons.get(buttonIndex));
            if (buttonIndex == buttons.size() - 1) {
                keyboard.add(row);
                if (withPreviousButton) {
                    row = new KeyboardRow();
                    row.add(WaiterConst.PREVIOUS);
                    keyboard.add(row);
                }
            } else if (buttonInRow >=3) {
                buttonInRow = 0;
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getPreviousButton() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(WaiterConst.PREVIOUS);
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboardMarkup(Map<String, String> map) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(new ArrayList<>());

        int rowIndex = 0;
        int buttonInRow = 0;

        for(Map.Entry<String, String> entry: map.entrySet()) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(entry.getKey());
            inlineKeyboardButton.setCallbackData(entry.getValue());
            rowList.get(rowIndex).add(inlineKeyboardButton);

            if (buttonInRow >= 2) {
                buttonInRow = 0;
                rowIndex++;
                rowList.add(new ArrayList<>());
            } else {
                buttonInRow++;
            }
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
