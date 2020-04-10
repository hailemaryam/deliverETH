package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitngForFinishOrderResponseProcessor {
    private final RequestForMenu requestForMenu;
    private final RequestFoodList requestFoodList;
    private final RequestForFinishOrder requestForFinishOrder;
    private final DbUtility dbUtility;
    private final CommandProcessor commandProcessor;

    public WaitngForFinishOrderResponseProcessor(RequestForMenu requestForMenu, RequestFoodList requestFoodList, RequestForFinishOrder requestForFinishOrder, DbUtility dbUtility, CommandProcessor commandProcessor) {
        this.requestForMenu = requestForMenu;
        this.requestFoodList = requestFoodList;
        this.requestForFinishOrder = requestForFinishOrder;
        this.dbUtility = dbUtility;
        this.commandProcessor = commandProcessor;
    }

    void processOrderFinishOrAddMoreItem(Update update, TelegramUserDTO telegramUser) {
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
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
