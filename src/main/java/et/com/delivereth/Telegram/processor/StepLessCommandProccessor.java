package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.Constants.BotCommands;
import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class StepLessCommandProccessor {
    private final StepProcessors stepProcessors;
    private final MainStepProccessor mainStepProccessor;

    public StepLessCommandProccessor(StepProcessors stepProcessors, MainStepProccessor mainStepProccessor) {
        this.stepProcessors = stepProcessors;
        this.mainStepProccessor = mainStepProccessor;
    }

    public void commandProccessor(Update update, TelegramUserDTO telegramUser){
        switch (update.getMessage().getText()) {
            case StaticText.cancelOrder:
            case BotCommands.cancel:
                stepProcessors.cancelOrder(update, telegramUser);
                break;
            case StaticText.newOrder:
            case BotCommands.newOrder:
                stepProcessors.newOrder(update, telegramUser);
                break;
            case StaticText.myOrders:
            case BotCommands.myOrders:
                stepProcessors.myOrder(update, telegramUser);
                break;
            case StaticText.help:
            case BotCommands.help:
                stepProcessors.help(update, telegramUser);
                break;
            default:
                mainStepProccessor.mainStepProcessor(update, telegramUser);
                break;
        }
    }
}
