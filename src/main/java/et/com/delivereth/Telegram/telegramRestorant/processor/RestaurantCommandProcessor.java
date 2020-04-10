package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestErrorResponder;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestForHelp;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantCommandProcessor {
    private final RestaurantRequestErrorResponder restaurantRequestErrorResponder;
    private final RestaurantRequestForHelp restaurantRequestForHelp;

    public RestaurantCommandProcessor(RestaurantRequestErrorResponder restaurantRequestErrorResponder, RestaurantRequestForHelp restaurantRequestForHelp) {
        this.restaurantRequestErrorResponder = restaurantRequestErrorResponder;
        this.restaurantRequestForHelp = restaurantRequestForHelp;
    }

    void help(Update update, TelegramRestaurantUserDTO telegramUser){
        restaurantRequestForHelp.helpResponse(update);
    }
    void myOrder(Update update, TelegramRestaurantUserDTO telegramUser){
    }

    void requestForErrorResponder(Update update, TelegramRestaurantUserDTO telegramUser) {
        restaurantRequestErrorResponder.userErrorResponseResponder(update);
    }

}
