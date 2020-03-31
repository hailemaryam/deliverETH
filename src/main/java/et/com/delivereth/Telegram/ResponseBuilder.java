package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.Food;
import et.com.delivereth.domain.Order;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.domain.enumeration.OrderStatus;
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
    private final RequestForHelp requestForHelp;
    private final DbUtility dbUtility;

    public ResponseBuilder(RequestContact requestContact, RequestForOrder requestForOrder, RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity, RequestForFinishOrder requestForFinishOrder, RequestErrorResponder requestErrorResponder, RequestForHelp requestForHelp, DbUtility dbUtility) {
        this.requestContact = requestContact;
        this.requestForOrder = requestForOrder;
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
        this.requestForFinishOrder = requestForFinishOrder;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForHelp = requestForHelp;
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
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_RESPONSE)) {
                processOrderRequestAndProceedToLocationRequest(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE)) {
                processLocationRequestAndProceedToRestorantRequest(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION)) {
                processRestorantSelectionAndProceedToFoodSelection(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM)) {
                processAddItemAndRequestQuanity(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY)) {
                processSetQuantityAndPreceedToPlaceOrder(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER)) {
                processOrderFinishOrAddMoreItem(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ERROR_PAGE_RESPONSE)) {
                processErrorResponse(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_HELP_PAGE_RESPONSE)) {
                processHelpResponse(update, telegramUser);
            } else {
                requestForErrorResponder(update, telegramUser);
            }
        }
    }
    public void requestForErrorResponder(Update update, TelegramUser telegramUser) {
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ERROR_PAGE_RESPONSE);
        requestErrorResponder.userErrorResponseResponder(update);
    }

    public void processContactAndProceedToOrder(Update update, TelegramUser telegramUser) {
        if (update.getMessage().getContact() != null) {
            dbUtility.registerUserPhone(update, telegramUser);
            requestForOrder.requestForOrder(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }

    public void processOrderRequestAndProceedToLocationRequest(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else if (update.hasMessage() && update.getMessage().getText().equals("order")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }

    public void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUser telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null) {
            Order order = dbUtility.registerOrder(update, telegramUser);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION);
            requestRestorantSelection.requestRestorantSelection(update, telegramUser, order);
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        }
    }

    public void processRestorantSelectionAndProceedToFoodSelection(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("menu_")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
            requestFoodList.requestFoodList(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }
    public void processAddItemAndRequestQuanity(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("food_")) {
            dbUtility.addFoodToOrder(update, telegramUser);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
            requestQuantity.requestQuantity(update);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    /* remaining */
    public void processSetQuantityAndPreceedToPlaceOrder(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith("quantity_")) {
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

    /* remaining */
    public void processOrderFinishOrAddMoreItem(Update update, TelegramUser telegramUser) {

    }
    public void processErrorResponse(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("help")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_HELP_PAGE_RESPONSE);
            requestForHelp.requestForHelp(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_RESPONSE);
            requestForOrder.requestForOrder(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }
    public void processHelpResponse(Update update, TelegramUser telegramUser) {
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("ok")) {
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_RESPONSE);
                requestForOrder.requestForOrder(update, telegramUser);
            } else {
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ERROR_PAGE_RESPONSE);
                requestForErrorResponder(update, telegramUser);
            }
        } else {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ERROR_PAGE_RESPONSE);
            requestForErrorResponder(update, telegramUser);
        }
    }
}
