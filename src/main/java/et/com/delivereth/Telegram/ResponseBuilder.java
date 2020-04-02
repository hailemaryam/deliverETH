package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.Order;
import et.com.delivereth.service.dto.OrderDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final RequestContact requestContact;
    private final RequestForMenu requestForMenu;
    private final RequestLocation requestLocation;
    private final RequestRestorantSelection requestRestorantSelection;
    private final RequestFoodList requestFoodList;
    private final RequestQuantity requestQuantity;
    private final RequestForFinishOrder requestForFinishOrder;
    private final RequestErrorResponder requestErrorResponder;
    private final DbUtility dbUtility;

    public ResponseBuilder(RequestContact requestContact, RequestForMenu requestForMenu, RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity, RequestForFinishOrder requestForFinishOrder, RequestErrorResponder requestErrorResponder, DbUtility dbUtility) {
        this.requestContact = requestContact;
        this.requestForMenu = requestForMenu;
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
        this.requestForFinishOrder = requestForFinishOrder;
        this.requestErrorResponder = requestErrorResponder;
        this.dbUtility = dbUtility;
    }

    public void getResponse(Update update) {
        TelegramUserDTO telegramUser = dbUtility.getTelegramUser(update);
        logger.error("telegram user {}" + telegramUser);
        if (telegramUser == null) {
            dbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else {
            if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE)) {
                processContactAndProceedToOrder(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE)) {
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
            } else {
                requestForErrorResponder(update, telegramUser);
            }
        }
    }

    public void requestForErrorResponder(Update update, TelegramUserDTO telegramUser) {
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        requestErrorResponder.userErrorResponseResponder(update);
        requestForMenu.requestForMenu(update, telegramUser);
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
    }

    public void processContactAndProceedToOrder(Update update, TelegramUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            dbUtility.registerUserPhone(update, telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }

    public void processOrderRequestAndProceedToLocationRequest(Update update, TelegramUserDTO telegramUser) {
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

    public void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null) {
            OrderDTO order = dbUtility.registerOrder(update, telegramUser);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION);
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        }
    }

    public void processRestorantSelectionAndProceedToFoodSelection(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("menu_")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
            requestFoodList.requestFoodList(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("loadMore")) {
            requestFoodList.requestFoodList(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    public void processAddItemAndRequestQuanity(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("food_")) {
            dbUtility.addFoodToOrder(update, telegramUser);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
            requestQuantity.requestQuantity(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("loadMore")) {
            requestFoodList.requestFoodList(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    public void processSetQuantityAndPreceedToPlaceOrder(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("quantity_")) {
            dbUtility.setQuantity(update, telegramUser);
            requestForFinishOrder.requestForFinishOrder(update, telegramUser);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("next")) {
            requestQuantity.requestQuantity(update, telegramUser);
        } else if (update.hasMessage()) {
            try {
                Integer.valueOf(update.getMessage().getText());
                dbUtility.setQuantity(update, telegramUser);
                requestForFinishOrder.requestForFinishOrder(update, telegramUser);
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER);
            } catch (NumberFormatException e) {
                requestForErrorResponder(update, telegramUser);
            }
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    public void processOrderFinishOrAddMoreItem(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("finishOrder")) {
            requestForFinishOrder.responseForFinishOrder(update);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
            dbUtility.finishOrder(telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("addMoreItem")) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
            requestFoodList.requestFoodList(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("cancelOrder")) {
            requestForFinishOrder.responseForCancelOrder(update);
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE);
            dbUtility.cancelOrder(telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }
}
