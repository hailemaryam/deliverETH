package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.TelegramUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);

    private final RequestContact requestContact;
    private final RequestForOrder requestForOrder;
    private final RequestLocation requestLocation;
    private final RequestRestorantSelection requestRestorantSelection;
    private final RequestFoodList requestFoodList;
    private final RequestQuantity requestQuantity;
    private final RequestErrorResponder requestErrorResponder;
    private final InlineButtonTest inlineButtonTest;
    private final DbUtility dbUtility;

    public ResponseBuilder(RequestContact requestContact, RequestLocation requestLocation, InlineButtonTest inlineButtonTest, DbUtility dbUtility, RequestErrorResponder requestErrorResponder, RequestForOrder requestForOrder, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity) {
        this.requestContact = requestContact;
        this.requestLocation = requestLocation;
        this.inlineButtonTest = inlineButtonTest;
        this.dbUtility = dbUtility;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForOrder = requestForOrder;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
    }

    public void getResponse(Update update) {
        TelegramUser telegramUser = dbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            dbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else {
            if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE)) {
                processContactAndProceedToOrder(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_RESPONSE)){
                processOrderRequestAndProceedToLocationRequest(update, telegramUser);
            } else if(telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE)){
                processLocationRequestAndProceedToRestorantRequest(update, telegramUser);
            } else if(telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION)) {

            } else {
                requestErrorResponder.userErrorResponseResponder(update);
            }
        }
    }
    public void processContactAndProceedToOrder (Update update, TelegramUser telegramUser) {
        if (update.getMessage().getContact() != null) {
            dbUtility.registerUserPhone(update ,telegramUser);
            requestForOrder.requestForOrder(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }
    public void processOrderRequestAndProceedToLocationRequest(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }
    public void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUser telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null){
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION);
            requestRestorantSelection.requestRestorantSelection(update);
        } else if((update.hasMessage() && update.getMessage().getLocation() == null)|| update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        }
    }
    public void processRestorantSelectionAndProceedToItemSelection(Update update, TelegramUser telegramUser){

    }
}
