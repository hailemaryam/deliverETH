package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.Constants.ChatStepConstants;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainStepProccessor {
    private final CommandProcessor commandProcessor;
    private final WaitingForAddItemResponceProcessor waitingForAddItemResponceProcessor;
    private final WaitingForContactResponseProcessor waitingForContactResponseProcessor;
    private final WaitingForLocationResponseProcessor waitingForLocationResponseProcessor;
    private final WaitingForMenuPageResponseProcessor waitingForMenuPageResponseProcessor;
    private final WaitingForMyOrderListResponseProcessor waitingForMyOrderListResponseProcessor;
    private final WaitingForRestaurantSelectionResponseProccessor waitingForRestaurantSelectionResponseProccessor;
    private final WaitingForSetQuantityResponseProcessor waitingForSetQuantityResponseProcessor;
    private final WaitngForFinishOrderResponseProcessor waitngForFinishOrderResponseProcessor;

    public MainStepProccessor(CommandProcessor commandProcessor, WaitingForAddItemResponceProcessor waitingForAddItemResponceProcessor, WaitingForContactResponseProcessor waitingForContactResponseProcessor, WaitingForLocationResponseProcessor waitingForLocationResponseProcessor, WaitingForMenuPageResponseProcessor waitingForMenuPageResponseProcessor, WaitingForMyOrderListResponseProcessor waitingForMyOrderListResponseProcessor, WaitingForRestaurantSelectionResponseProccessor waitingForRestaurantSelectionResponseProccessor, WaitingForSetQuantityResponseProcessor waitingForSetQuantityResponseProcessor, WaitngForFinishOrderResponseProcessor waitngForFinishOrderResponseProcessor) {
        this.commandProcessor = commandProcessor;
        this.waitingForAddItemResponceProcessor = waitingForAddItemResponceProcessor;
        this.waitingForContactResponseProcessor = waitingForContactResponseProcessor;
        this.waitingForLocationResponseProcessor = waitingForLocationResponseProcessor;
        this.waitingForMenuPageResponseProcessor = waitingForMenuPageResponseProcessor;
        this.waitingForMyOrderListResponseProcessor = waitingForMyOrderListResponseProcessor;
        this.waitingForRestaurantSelectionResponseProccessor = waitingForRestaurantSelectionResponseProccessor;
        this.waitingForSetQuantityResponseProcessor = waitingForSetQuantityResponseProcessor;
        this.waitngForFinishOrderResponseProcessor = waitngForFinishOrderResponseProcessor;
    }

    public void mainStepProcessor(Update update, TelegramUserDTO telegramUser){
        switch (telegramUser.getConversationMetaData()) {
            case ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE:
                waitingForContactResponseProcessor.processContactAndProceedToOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE:
                waitingForMenuPageResponseProcessor.processOrderMenuRequestAndProceedToLocationRequest(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE:
                waitingForLocationResponseProcessor.processLocationRequestAndProceedToRestorantRequest(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION:
                waitingForRestaurantSelectionResponseProccessor.processRestorantSelectionAndProceedToFoodSelection(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM:
                waitingForAddItemResponceProcessor.processAddItemAndRequestQuanity(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY:
                waitingForSetQuantityResponseProcessor.processSetQuantityAndPreceedToPlaceOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER:
                waitngForFinishOrderResponseProcessor.processOrderFinishOrAddMoreItem(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE:
                waitingForMyOrderListResponseProcessor.processMyOrderResponse(update, telegramUser);
                break;
            default:
                commandProcessor.requestForErrorResponder(update, telegramUser);
                break;
        }
    }
}
