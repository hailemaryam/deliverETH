package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.enumeration.OrderStatus;
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
    private final RequestForMyOrdersList requestForMyOrdersList;
    private final RequestForHelp requestForHelp;
    private final DbUtility dbUtility;

    public ResponseBuilder(RequestContact requestContact, RequestForMenu requestForMenu, RequestLocation requestLocation, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity, RequestForFinishOrder requestForFinishOrder, RequestErrorResponder requestErrorResponder, RequestForMyOrdersList requestForMyOrdersList, RequestForHelp requestForHelp, DbUtility dbUtility) {
        this.requestContact = requestContact;
        this.requestForMenu = requestForMenu;
        this.requestLocation = requestLocation;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
        this.requestForFinishOrder = requestForFinishOrder;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForMyOrdersList = requestForMyOrdersList;
        this.requestForHelp = requestForHelp;
        this.dbUtility = dbUtility;
    }

    public void getResponse(Update update) {
        TelegramUserDTO telegramUser = dbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            dbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else if (update.hasMessage() && update.getMessage().hasText() &&
            (update.getMessage().getText().equals("Cancel Order") || update.getMessage().getText().equals("/cancel"))) {
            cancelOrder(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().hasText() &&
            (update.getMessage().getText().equals("New Order") || update.getMessage().getText().equals("/new_order"))) {
            cancelOrder(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().hasText() &&
            (update.getMessage().getText().equals("My Orders") || update.getMessage().getText().equals("/my_orders"))) {
            newOrder(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().hasText() &&
            (update.getMessage().getText().equals("Help") || update.getMessage().getText().equals("/help"))) {
            help(update, telegramUser);
        } else {
            if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE)) {
                processContactAndProceedToOrder(update, telegramUser);
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE)) {
                processOrderMenuRequestAndProceedToLocationRequest(update, telegramUser);
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
            } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE)) {
                processMyOrderResponse(update, telegramUser);
            } else {
                requestForErrorResponder(update, telegramUser);
            }
        }
    }

    public void cancelOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        requestForMenu.requestForMenu(update, telegramUser);
    }
    public void newOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
        requestLocation.requestLocation(update);
    }
    public void help(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        requestForHelp.helpResponse(update);
        requestForMenu.requestForMenu(update, telegramUser);
    }
    public void myOrder(Update update, TelegramUserDTO telegramUser){
        if (Integer.valueOf(telegramUser.getConversationMetaData()) <= 7) {
            dbUtility.cancelOrder(telegramUser);
        }
        dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
        requestForMyOrdersList.sendTitle(update);
        requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
    }

    public void requestForErrorResponder(Update update, TelegramUserDTO telegramUser) {
        requestErrorResponder.userErrorResponseResponder(update);
        cancelOrder(update, telegramUser);
    }

    public void processContactAndProceedToOrder(Update update, TelegramUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            dbUtility.registerUserPhone(update, telegramUser);
            requestForMenu.requestForMenu(update, telegramUser);
        } else {
            requestContact.requestContactAgain(update);
        }
    }

    public void processOrderMenuRequestAndProceedToLocationRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order") ||
            (update.hasMessage() && update.getMessage().getText().equals("New Order"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else if ((update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("myOrder")) ||
            (update.hasMessage() && update.getMessage().getText().equals("My Orders"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
            requestForMyOrdersList.sendTitle(update);
            requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().getText().equals("Help")) {

        } else if (update.hasMessage() && update.getMessage().getText().equals("Setting")) {

        } else {
            requestErrorResponder.userErrorResponseResponder(update);
        }
    }

    public void processLocationRequestAndProceedToRestorantRequest(Update update, TelegramUserDTO telegramUser) {
        if (update.hasMessage() && update.getMessage().getLocation() != null) {
            dbUtility.registerOrder(update, telegramUser);
            requestRestorantSelection.sendTitle(update);
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else if ((update.hasMessage() && update.getMessage().getLocation() == null) || update.hasCallbackQuery()) {
            requestLocation.requestLocationAgain(update);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    public void processRestorantSelectionAndProceedToFoodSelection(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("menu_")) {
            try {
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
                telegramUser.setSelectedRestorant(Long.valueOf(update.getCallbackQuery().getData().substring(5)));
                dbUtility.updateTelegramUser(telegramUser);
                requestFoodList.requestFoodList(update, telegramUser);
            } catch (NumberFormatException e) {
                requestForErrorResponder(update, telegramUser);
            }
        } else if(update.hasMessage() && update.getMessage().getText().startsWith("/show_menu_")) {
            try {
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
                telegramUser.setSelectedRestorant(Long.valueOf(update.getMessage().getText().substring(11)));
                dbUtility.updateTelegramUser(telegramUser);
                requestFoodList.requestFoodList(update, telegramUser);
            } catch (NumberFormatException e) {
                requestForErrorResponder(update, telegramUser);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("loadMore")) {
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }

    public void processAddItemAndRequestQuanity(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("food_")) {
            try {
                dbUtility.addFoodToOrder(telegramUser,Long.valueOf(update.getCallbackQuery().getData().substring(5)));
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
                requestQuantity.requestQuantity(update, telegramUser);
            } catch (NumberFormatException e) {
                requestForErrorResponder(update, telegramUser);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("next")) {
            requestFoodList.requestFoodListNext(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("prev")) {
            requestFoodList.requestFoodListPrev(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/add_to_cart_")) {
            try {
                dbUtility.addFoodToOrder(telegramUser, Long.valueOf(update.getMessage().getText().substring(13)));
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
                requestQuantity.requestQuantity(update, telegramUser);
            } catch (NumberFormatException e) {
                requestForErrorResponder(update, telegramUser);
            }
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
            requestQuantity.requestQuantityNextPage(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("prev")) {
            requestQuantity.requestQuantityPrevious(update, telegramUser);
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

    public void processMyOrderResponse(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("C_")) {
            dbUtility.changeOrderStatusById(Long.valueOf(update.getCallbackQuery().getData().substring(2)), OrderStatus.CANCELED_BY_USER);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("R_")) {
            dbUtility.orderRemove(Long.valueOf(update.getCallbackQuery().getData().substring(2)));
        }  else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("order") ||
            (update.hasMessage() && update.getMessage().getText().equals("New Order"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE);
            requestLocation.requestLocation(update);
        } else if ((update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("myOrder")) ||
            (update.hasMessage() && update.getMessage().getText().equals("My Orders"))) {
            dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE);
            requestForMyOrdersList.sendTitle(update);
            requestForMyOrdersList.requestForMyOrdersList(update, telegramUser);
        } else {
            requestForErrorResponder(update, telegramUser);
        }
    }
}
