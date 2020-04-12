package et.com.delivereth.Telegram.telegramTransport.processor;

import et.com.delivereth.Telegram.telegramTransport.ChatStepConstants;
import et.com.delivereth.service.dto.TelegramDeliveryUserDTO;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportMainStepProccessor {
    private final TransportCommandProcessor transportCommandProcessor;
    private final TransportWaitingForContactResponseProcessor transportWaitingForContactResponseProcessor;
    private final TransportRestaurantWaitingForAccountLinkProcessor transportRestaurantWaitingForAccountLinkProcessor;
    private final TransportWaitingForOrderListProcessor transportWaitingForOrderListProcessor;

    public TransportMainStepProccessor(TransportCommandProcessor transportCommandProcessor, TransportWaitingForContactResponseProcessor transportWaitingForContactResponseProcessor, TransportRestaurantWaitingForAccountLinkProcessor transportRestaurantWaitingForAccountLinkProcessor, TransportWaitingForOrderListProcessor transportWaitingForOrderListProcessor) {
        this.transportCommandProcessor = transportCommandProcessor;
        this.transportWaitingForContactResponseProcessor = transportWaitingForContactResponseProcessor;
        this.transportRestaurantWaitingForAccountLinkProcessor = transportRestaurantWaitingForAccountLinkProcessor;
        this.transportWaitingForOrderListProcessor = transportWaitingForOrderListProcessor;
    }

    public void mainStepProcessor(Update update, TelegramDeliveryUserDTO telegramUser){
        switch (telegramUser.getConversationMetaData()) {
            case ChatStepConstants.WAITING_FOR_CONTACT_RESPONSE:
                transportWaitingForContactResponseProcessor.processContactAndProceedToOrder(update, telegramUser);
                break;
            case ChatStepConstants.WAITING_FOR_MY_ORDER_LIST_RESPONSE:
                transportWaitingForOrderListProcessor.processOrder(update, telegramUser);
                break;
            default:
                transportCommandProcessor.requestForErrorResponder(update, telegramUser);
                break;
        }
    }
}
