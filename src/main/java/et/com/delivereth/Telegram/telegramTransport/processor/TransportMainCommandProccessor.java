package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramUser.BotCommands;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportMainCommandProccessor {
    private final TransportCommandProcessor transportCommandProcessor;
    private final TransportMainStepProccessor transportMainStepProccessor;

    public TransportMainCommandProccessor(TransportCommandProcessor transportCommandProcessor, TransportMainStepProccessor transportMainStepProccessor) {
        this.transportCommandProcessor = transportCommandProcessor;
        this.transportMainStepProccessor = transportMainStepProccessor;
    }

    public void commandProccessor(Update update, TelegramDeliveryUserDTO telegramUser){
        switch (update.getMessage().getText()) {
//            case StaticText.cancelOrder:
//            case BotCommands.cancel:
//                restaurantCommandProcessor.cancelOrder(update, telegramUser);
//                break;
//            case StaticText.newOrder:
//            case BotCommands.newOrder:
//                restaurantCommandProcessor.newOrder(update, telegramUser);
//                break;
//            case StaticText.myOrders:
//            case BotCommands.myOrders:
//                restaurantCommandProcessor.myOrder(update, telegramUser);
//                break;
            case StaticText.help:
            case BotCommands.help:
                transportCommandProcessor.help(update, telegramUser);
                break;
            default:
                transportMainStepProccessor.mainStepProcessor(update, telegramUser);
                break;
        }
    }
}
