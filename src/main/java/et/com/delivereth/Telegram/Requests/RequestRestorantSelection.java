package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
public class RequestRestorantSelection {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;

    public RequestRestorantSelection(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestRestorantSelection(Update update, TelegramUserDTO telegramUser) {
        Page<RestorantDTO> restorantList = dbUtility.getRestorantList(telegramUser);
        restorantList.toList().forEach(restorantDTO -> {
            sendRestorant(restorantDTO, update);
        });
        if (restorantList.toList().size() == 0) {
            sendNoMoreItem(update);
            dbUtility.cancelOrder(telegramUser);
        } else if (restorantList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }
    private void sendRestorant(RestorantDTO restorantDTO, Update update){
        SendPhoto response = new SendPhoto();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        InputStream inputStream = new ByteArrayInputStream(restorantDTO.getIconImage());
        response.setPhoto(restorantDTO.getName(), inputStream);
        String caption = "<b>"+restorantDTO.getName() + "</b>\n" +
            "<i>To View Menu: </i><a>/SHOW_MENU_" + restorantDTO.getId() + "</a>";
        response.setCaption(caption);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private void sendLoadMoreButton(Update update){
        SendMessage response = new SendMessage();
        response.setReplyMarkup(Menu.loadMore());
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Want to list more restaurant.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    private void sendNoMoreItem(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("There are no restaurant list around you delivering at this time.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void sendTitle(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>Choose restaurant from this list</b>");
        response.setParseMode("HTML");
        response.setReplyMarkup(Menu.orderKeyBoardMenu());
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
