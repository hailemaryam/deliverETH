package et.com.delivereth.Telegram.processor;

import et.com.delivereth.Telegram.Constants.BotCommands;
import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainCommandProccessor {
    private final CommandProcessor commandProcessor;
    private final MainStepProccessor mainStepProccessor;

    public MainCommandProccessor(CommandProcessor commandProcessor, MainStepProccessor mainStepProccessor) {
        this.commandProcessor = commandProcessor;
        this.mainStepProccessor = mainStepProccessor;
    }

    public void commandProccessor(Update update, TelegramUserDTO telegramUser){
        switch (update.getMessage().getText()) {
            case StaticText.cancelOrder:
            case BotCommands.cancel:
                commandProcessor.cancelOrder(update, telegramUser);
                break;
            case StaticText.newOrder:
            case BotCommands.newOrder:
                commandProcessor.newOrder(update, telegramUser);
                break;
            case StaticText.myOrders:
            case BotCommands.myOrders:
                commandProcessor.myOrder(update, telegramUser);
                break;
            case StaticText.help:
            case BotCommands.help:
                commandProcessor.help(update, telegramUser);
                break;
            default:
                mainStepProccessor.mainStepProcessor(update, telegramUser);
                break;
        }
    }
}
