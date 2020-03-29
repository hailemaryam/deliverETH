package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.TelegramUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private final RequestForFinishOrder requestForFinishOrder;
    private final RequestErrorResponder requestErrorResponder;
    private final InlineButtonTest inlineButtonTest;
    private final DbUtility dbUtility;

    public ResponseBuilder(RequestContact requestContact, RequestForOrder requestForOrder, RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity, RequestForFinishOrder requestForFinishOrder, RequestErrorResponder requestErrorResponder, InlineButtonTest inlineButtonTest, DbUtility dbUtility) {
        this.requestContact = requestContact;
        this.requestForOrder = requestForOrder;
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
        this.requestForFinishOrder = requestForFinishOrder;
        this.requestErrorResponder = requestErrorResponder;
        this.inlineButtonTest = inlineButtonTest;
        this.dbUtility = dbUtility;
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
                processRestorantSelectionAndProceedToFoodSelection(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM)){
                processAddItemAndRequestQuanity(update, telegramUser);
            } else if(telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY)){
                processSetQuantityAndPreceedToPlaceOrder(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER)) {

            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_FOLLOW_ORDER_STATUS)) {

            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_HELP_OR_PROCEED_RESPONSE)){

            } else {
                requestForErrorResponder(update, telegramUser);
            }
        }
    }
    public void requestForErrorResponder(Update update, TelegramUser telegramUser) {
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            // cancel order first
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION);
        requestErrorResponder.userErrorResponseResponder(update);
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
    /* remaining */
    public void processRestorantSelectionAndProceedToFoodSelection(Update update, TelegramUser telegramUser){
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("menu_")){
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
            requestFoodList.requestFoodList(update);
        } else {

        }
    }
    /* remaining */
    public void processAddItemAndRequestQuanity(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("food_")){
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
            requestQuantity.requestQuantity(update);
        } else {

        }
    }
    public void processSetQuantityAndPreceedToPlaceOrder(Update update, TelegramUser telegramUser){
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith("quantity_page_")) {
                // update list to paget by substring
            } else {
                try {
                    Integer quantity = Integer.valueOf(update.getCallbackQuery().getData());
                    // add quantity
                    requestForFinishOrder.requestForFinishOrder(update, telegramUser);
                    dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER);
                } catch (NumberFormatException e) {
                    requestForErrorResponder(update, telegramUser);
                }
            }
        } else if (update.hasMessage()) {
            Integer quantity = Integer.valueOf(update.getMessage().getText());

        }
    }
}
