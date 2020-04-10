package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class CommandProcessor {
    private final RequestForMenu requestForMenu;
    private final RequestLocation requestLocation;
    private final RequestErrorResponder requestErrorResponder;
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final RequestForHelp requestForHelp;
    private final DbUtility dbUtility;

    public CommandProcessor(RequestForMenu requestForMenu, RequestLocation requestLocation, RequestErrorResponder requestErrorResponder, RequestForMyOrdersList requestForMyOrdersList, RequestForHelp requestForHelp, DbUtility dbUtility) {
        this.requestForMenu = requestForMenu;
        this.requestLocation = requestLocation;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.requestForHelp = requestForHelp;
        this.dbUtility = dbUtility;
    }

    void cancelOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        requestForMenu.requestForMenu(update, telegramUser);
    }
    void newOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
        requestLocation.requestLocation(update);
    }
    void help(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        requestForHelp.helpResponse(update);
        requestForMenu.requestForMenu(update, telegramUser);
    }
    void myOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
        requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
    }

    void requestForErrorResponder(Update update, TelegramUserDTO telegramUser) {
        requestErrorResponder.userErrorResponseResponder(update);
        cancelOrder(update, telegramUser);
    }

}
