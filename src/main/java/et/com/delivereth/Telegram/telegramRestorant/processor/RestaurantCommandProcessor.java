package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestErrorResponder;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import et.com.delivereth.service.dto.TelegramUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantCommandProcessor {
    private final RestaurantRequestErrorResponder restaurantRequestErrorResponder;

    public RestaurantCommandProcessor(RestaurantRequestErrorResponder restaurantRequestErrorResponder) {
        this.restaurantRequestErrorResponder = restaurantRequestErrorResponder;
    }

    void help(Update update, TelegramUserDTO telegramUser){
    }
    void myOrder(Update update, TelegramUserDTO telegramUser){
    }

    void requestForErrorResponder(Update update, TelegramRestaurantUserDTO telegramUser) {
        restaurantRequestErrorResponder.userErrorResponseResponder(update);
    }

}
