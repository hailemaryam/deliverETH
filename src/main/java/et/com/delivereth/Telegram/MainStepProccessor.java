package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Constants.ChatStepConstants;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainStepProccessor {
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final StepProcessors stepProcessors;

    public MainStepProccessor(StepProcessors stepProcessors) {
        this.stepProcessors = stepProcessors;
    }

    public void mainStepProcessor(Update update, TelegramUserDTO telegramUser){
        if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE)) {
            stepProcessors.processContactAndProceedToOrder(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_MENU_PAGE_RESPONSE)) {
            stepProcessors.processOrderMenuRequestAndProceedToLocationRequest(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_LOCATION_RESPONSE)) {
            stepProcessors.processLocationRequestAndProceedToRestorantRequest(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_RESTORANT_SELECTION)) {
            stepProcessors.processRestorantSelectionAndProceedToFoodSelection(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM)) {
            stepProcessors.processAddItemAndRequestQuanity(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY)) {
            stepProcessors.processSetQuantityAndPreceedToPlaceOrder(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_ORDER_LOOP_FINISH_ORDER)) {
            stepProcessors.processOrderFinishOrAddMoreItem(update, telegramUser);
        } else if (telegramUser.getConversationMetaData().equals(ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE)) {
            stepProcessors.processMyOrderResponse(update, telegramUser);
        } else {
            stepProcessors.requestForErrorResponder(update, telegramUser);
        }
    }
}
