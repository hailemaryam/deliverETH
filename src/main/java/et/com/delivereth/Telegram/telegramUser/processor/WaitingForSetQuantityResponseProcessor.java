package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForSetQuantityResponseProcessor {
    private final RequestQuantity requestQuantity;
    private final RequestForFinishOrder requestForFinishOrder;
    private final DbUtility dbUtility;
    private final CommandProcessor commandProcessor;

    public WaitingForSetQuantityResponseProcessor(RequestQuantity requestQuantity, RequestForFinishOrder requestForFinishOrder, DbUtility dbUtility, CommandProcessor commandProcessor) {
        this.requestQuantity = requestQuantity;
        this.requestForFinishOrder = requestForFinishOrder;
        this.dbUtility = dbUtility;
        this.commandProcessor = commandProcessor;
    }

    void processSetQuantityAndPreceedToPlaceOrder(Update update, TelegramUserDTO telegramUser) {
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
                commandProcessor.requestForErrorResponder(update, telegramUser);
            }
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
