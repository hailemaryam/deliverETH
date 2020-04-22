package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.KeyValuePairDbUtility;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
import et.com.delivereth.service.dto.KeyValuPairHolderDTO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Service
public class RequestLocation {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final KeyValuePairDbUtility keyValuePairDbUtility;

    public RequestLocation(TelegramSender telegramSender, KeyValuePairDbUtility keyValuePairDbUtility) {
        this.telegramSender = telegramSender;
        this.keyValuePairDbUtility = keyValuePairDbUtility;
    }

    public void requestLocation(Update update) {
        requestLocation(update, StaticText.weNeedYourLocationText);
    }
    public void requestLocationAgain(Update update) {
        requestLocation(update, StaticText.weNeedYourLocationAgainText);
    }
    public void requestForErrorOutofAddiss(Update update){
        requestLocation(update, StaticText.locationOutOfAddisError);
    }
    public void requestLocation(Update update, String text){
        try {
            SendPhoto response = new SendPhoto();
            response.setReplyMarkup(Menu.prepareShareLocationReplyButton());
            if (update.hasMessage()){
                response.setChatId(update.getMessage().getChatId());
            } else if (update.hasCallbackQuery()) {
                response.setChatId(update.getCallbackQuery().getMessage().getChatId());
            }
            KeyValuPairHolderDTO keyValuPairHolderDTO = keyValuePairDbUtility.getByKey("turnOnLocation");
            File file = new File("turnOnImage");
            FileUtils.writeByteArrayToFile(file, keyValuPairHolderDTO.getValueImage());
            response.setPhoto(file);
            response.setCaption(text);
            try {
                telegramSender.execute(response);
            } catch (TelegramApiException e) {
                logger.error("Error Sending Message {}", response);
            }
        } catch (Exception e) {
            sendTextMessage(update, text);
        }
    }
    public void sendTextMessage(Update update, String text){
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.prepareShareLocationReplyButton());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText(text);
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
