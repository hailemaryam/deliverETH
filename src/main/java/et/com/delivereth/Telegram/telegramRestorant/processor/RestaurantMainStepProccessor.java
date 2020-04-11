package et.com.delivereth.Telegram.telegramRestorant.processor;

import et.com.delivereth.Telegram.telegramRestorant.ChatStepConstants;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class RestaurantMainStepProccessor {
    private final RestaurantCommandProcessor restaurantCommandProcessor;
    private final RestaurantWaitingForContactResponseProcessor restaurantWaitingForContactResponseProcessor;
    private final RestaurantWaitingForAccountLinkProcessor restaurantWaitingForAccountLinkProcessor;
    private final RestaurantWaitingForOrderListProcessor restaurantWaitingForOrderListProcessor;

    public RestaurantMainStepProccessor(RestaurantCommandProcessor restaurantCommandProcessor, RestaurantWaitingForContactResponseProcessor restaurantWaitingForContactResponseProcessor, RestaurantWaitingForAccountLinkProcessor restaurantWaitingForAccountLinkProcessor, RestaurantWaitingForOrderListProcessor restaurantWaitingForOrderListProcessor) {
        this.restaurantCommandProcessor = restaurantCommandProcessor;
        this.restaurantWaitingForContactResponseProcessor = restaurantWaitingForContactResponseProcessor;
        this.restaurantWaitingForAccountLinkProcessor = restaurantWaitingForAccountLinkProcessor;
        this.restaurantWaitingForOrderListProcessor = restaurantWaitingForOrderListProcessor;
    }

    public void mainStepProcessor(Update update, TelegramRestaurantUserDTO telegramUser){
        switch (telegramUser.getConversationMetaData()) {
            case ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE:
                restaurantWaitingForContactResponseProcessor.processContactAndProceedToOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_ACCOUNT_LINKING_RESPONSE:
                restaurantWaitingForAccountLinkProcessor.requestUserForAccountLinking(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE:
                restaurantWaitingForOrderListProcessor.processOrder(update, telegramUser);
                break;
            default:
                restaurantCommandProcessor.requestForErrorResponder(update, telegramUser);
                break;
        }
    }
}
