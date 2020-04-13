package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.RestorantDbUtitlity;
import et.com.delivereth.Telegram.telegramUser.main.TelegramHome;
import et.com.delivereth.Telegram.telegramUser.main.TelegramSender;
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
    private final RestorantDbUtitlity restorantDbUtitlity;

    public RequestRestorantSelection(TelegramSender telegramSender, DbUtility dbUtility, RestorantDbUtitlity restorantDbUtitlity) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
        this.restorantDbUtitlity = restorantDbUtitlity;
    }

    public void requestRestorantSelection(Update update, TelegramUserDTO telegramUser) {
        Page<RestorantDTO> restorantList = restorantDbUtitlity.getRestorantList(telegramUser);
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
            "<i>To View Menu: </i><a>/SHOW_" + restorantDTO.getUserName() + "_MENU</a>";
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
        response.setText(StaticText.wantToListMoreRestaurant);
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
        response.setText(StaticText.noRestaurantListAround);
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
        response.setText(StaticText.chooseRestaurantListTitle);
        response.setParseMode("HTML");
        response.setReplyMarkup(Menu.orderKeyBoardMenu(true));
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
