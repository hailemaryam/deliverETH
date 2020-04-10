package et.com.delivereth.Telegram.telegramUser.processor;

import et.com.delivereth.Telegram.telegramUser.ChatStepConstants;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.DbUtility.RestorantDbUtitlity;
import et.com.delivereth.Telegram.DbUtility.TelegramUserDbUtility;
import et.com.delivereth.Telegram.telegramUser.requests.*;
import et.com.delivereth.service.dto.RestorantDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class WaitingForRestaurantSelectionResponseProccessor {
    private final RequestRestorantSelection requestRestorantSelection;
    private final RequestFoodList requestFoodList;
    private final RestorantDbUtitlity restorantDbUtitlity;
    private final DbUtility dbUtility;
    private final CommandProcessor commandProcessor;
    private final TelegramUserDbUtility telegramUserDbUtility;

    public WaitingForRestaurantSelectionResponseProccessor(RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RestorantDbUtitlity restorantDbUtitlity, DbUtility dbUtility, CommandProcessor commandProcessor, TelegramUserDbUtility telegramUserDbUtility) {
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.restorantDbUtitlity = restorantDbUtitlity;
        this.dbUtility = dbUtility;
        this.commandProcessor = commandProcessor;
        this.telegramUserDbUtility = telegramUserDbUtility;
    }

    void processRestorantSelectionAndProceedToFoodSelection(Update update, TelegramUserDTO telegramUser) {
        if(update.hasMessage() && update.getMessage().getText().startsWith("/SHOW_")) {
            try {
                dbUtility.updateStep(telegramUser, ChatStepConstants.WAITING_FOR_ORDER_LOOP_ADD_ITEM);
                String restorantUserName = update.getMessage().getText().substring(6);
                restorantUserName = restorantUserName.substring(0, restorantUserName.length() - 5);
                RestorantDTO restorantDTO = restorantDbUtitlity.getRestorant(restorantUserName);
                telegramUser.setSelectedRestorant(restorantDTO.getId());
                telegramUserDbUtility.updateTelegramUser(telegramUser);
                requestFoodList.requestFoodList(update, telegramUser);
            } catch (NumberFormatException e) {
                commandProcessor.requestForErrorResponder(update, telegramUser);
            }
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("loadMore")) {
            requestRestorantSelection.requestRestorantSelection(update, telegramUser);
        } else {
            commandProcessor.requestForErrorResponder(update, telegramUser);
        }
    }
}
