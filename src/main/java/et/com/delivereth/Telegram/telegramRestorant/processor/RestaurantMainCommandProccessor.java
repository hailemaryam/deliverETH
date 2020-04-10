package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.Constants.StaticText;
import et.com.delivereth.Telegram.telegramUser.BotCommands;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantMainCommandProccessor {
    private final RestaurantCommandProcessor restaurantCommandProcessor;
    private final RestaurantMainStepProccessor restaurantMainStepProccessor;

    public RestaurantMainCommandProccessor(RestaurantCommandProcessor restaurantCommandProcessor, RestaurantMainStepProccessor restaurantMainStepProccessor) {
        this.restaurantCommandProcessor = restaurantCommandProcessor;
        this.restaurantMainStepProccessor = restaurantMainStepProccessor;
    }

    public void commandProccessor(Update update, TelegramRestaurantUserDTO telegramUser){
        switch (update.getMessage().getText()) {
            case StaticText.cancelOrder:
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
                restaurantCommandProcessor.help(update, telegramUser);
                break;
            default:
                restaurantMainStepProccessor.mainStepProcessor(update, telegramUser);
                break;
        }
    }
}
