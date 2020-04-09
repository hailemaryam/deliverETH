package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.Constants.ChatStepConstants;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainStepProccessor {
    private final StepProcessors stepProcessors;

    public MainStepProccessor(StepProcessors stepProcessors) {
        this.stepProcessors = stepProcessors;
    }

    public void mainStepProcessor(Update update, TelegramUserDTO telegramUser){
        switch (telegramUser.getConversationMetaData()) {
            case ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE:
                stepProcessors.processContactAndProceedToOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE:
                stepProcessors.processOrderMenuRequestAndProceedToLocationRequest(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE:
                stepProcessors.processLocationRequestAndProceedToRestorantRequest(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION:
                stepProcessors.processRestorantSelectionAndProceedToFoodSelection(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM:
                stepProcessors.processAddItemAndRequestQuanity(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY:
                stepProcessors.processSetQuantityAndPreceedToPlaceOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER:
                stepProcessors.processOrderFinishOrAddMoreItem(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE:
                stepProcessors.processMyOrderResponse(update, telegramUser);
                break;
            default:
                stepProcessors.requestForErrorResponder(update, telegramUser);
                break;
        }
    }
}
