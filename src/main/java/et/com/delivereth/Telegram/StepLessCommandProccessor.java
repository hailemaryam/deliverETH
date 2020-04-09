package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.DbUtility.DbUtility;
import et.com.delivereth.Telegram.Requests.RequestContact;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class StepLessCommandProccessor {
    private final RequestContact requestContact;
    private final DbUtility dbUtility;
    private final StepProcessors stepProcessors;
    private final MainStepProccessor mainStepProccessor;

    public StepLessCommandProccessor(RequestContact requestContact, DbUtility dbUtility, StepProcessors stepProcessors, MainStepProccessor mainStepProccessor) {
        this.requestContact = requestContact;
        this.dbUtility = dbUtility;
        this.stepProcessors = stepProcessors;
        this.mainStepProccessor = mainStepProccessor;
    }
    public void commandProccessor(Update update, TelegramUserDTO telegramUser){
        if (update.getMessage().getText().equals(StaticText.cancelOrder) || update.getMessage().getText().equals("/cancel")) {
            stepProcessors.cancelOrder(update, telegramUser);
        } else if (update.getMessage().getText().equals(StaticText.newOrder) || update.getMessage().getText().equals("/new_order")) {
            stepProcessors.newOrder(update, telegramUser);
        } else if (update.getMessage().getText().equals(StaticText.myOrders) || update.getMessage().getText().equals("/my_orders")) {
            stepProcessors.myOrder(update, telegramUser);
        } else if (update.getMessage().getText().equals(StaticText.help) || update.getMessage().getText().equals("/help")) {
            stepProcessors.help(update, telegramUser);
        } else {
            mainStepProccessor.mainStepProcessor(update, telegramUser);
        }
    }
}
