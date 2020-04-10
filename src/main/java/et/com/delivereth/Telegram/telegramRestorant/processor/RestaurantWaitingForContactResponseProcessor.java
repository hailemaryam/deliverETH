package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.DbUtility.TelegramRestaurantUserDbUtility;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestContact;
import et.com.delivereth.Telegram.telegramRestorant.requests.RestaurantRequestForAccountLiking;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantWaitingForContactResponseProcessor {
    private final RestaurantRequestContact restaurantRequestContact;
    private final RestaurantRequestForAccountLiking restaurantRequestForAccountLiking;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;

    public RestaurantWaitingForContactResponseProcessor(RestaurantRequestContact restaurantRequestContact, RestaurantRequestForAccountLiking restaurantRequestForAccountLiking, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility) {
        this.restaurantRequestContact = restaurantRequestContact;
        this.restaurantRequestForAccountLiking = restaurantRequestForAccountLiking;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
    }

    void processContactAndProceedToOrder(Update update, TelegramRestaurantUserDTO telegramUser) {
        if (update.getMessage().getContact() != null) {
            telegramRestaurantUserDbUtility.registerUserPhone(update, telegramUser);
            restaurantRequestForAccountLiking.requestForMenu(update, telegramUser);
        } else {
            restaurantRequestContact.requestContactAgain(update);
        }
    }

}
