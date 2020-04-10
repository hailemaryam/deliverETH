package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.FoodDbUtitility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForAddItemResponceProcessor {
    private final RequestFoodList requestFoodList;
    private final RequestQuantity requestQuantity;
    private final DbUtility dbUtility;
    private final CommandProcessor commandProcessor;
    private final FoodDbUtitility foodDbUtitility;

    public WaitingForAddItemResponceProcessor(RequestFoodList requestFoodList, RequestQuantity requestQuantity, DbUtility dbUtility, CommandProcessor commandProcessor, FoodDbUtitility foodDbUtitility) {
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
        this.dbUtility = dbUtility;
        this.commandProcessor = commandProcessor;
        this.foodDbUtitility = foodDbUtitility;
    }

    void processAddItemAndRequestQuanity(Update update, TelegramUserDTO telegramUser) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData().startsWith("food_")) {
            try {
                foodDbUtitility.addFoodToOrder(telegramUser,Long.valueOf(update.getCallbackQuery().getData().substring(5)));
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
                requestQuantity.requestQuantity(update, telegramUser);
            } catch (NumberFormatException e) {
                commandProcessor.requestForErrorResponder(update, telegramUser);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("next")) {
            requestFoodList.requestFoodListNext(update, telegramUser);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("prev")) {
            requestFoodList.requestFoodListPrev(update, telegramUser);
        } else if (update.hasMessage() && update.getMessage().getText().startsWith("/ADD_TO_CART_")) {
            try {
                foodDbUtitility.addFoodToOrder(telegramUser, Long.valueOf(update.getMessage().getText().substring(13)));
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_SET_QUANTITY);
                requestQuantity.requestQuantity(update, telegramUser);
            } catch (NumberFormatException e) {
                commandProcessor.requestForErrorResponder(update, telegramUser);
            }
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
