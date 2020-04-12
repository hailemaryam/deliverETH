package et.com.delivereth.Telegram.telegramTransport;

import et.com.delivereth.Telegram.DbUtility.TelegramRestaurantUserDbUtility;
import et.com.delivereth.Telegram.telegramTransport.processor.TransportMainCommandProccessor;
import et.com.delivereth.Telegram.telegramTransport.processor.TransportMainStepProccessor;
import et.com.delivereth.Telegram.telegramTransport.requests.TransportRequestContact;
import et.com.delivereth.service.dto.TelegramRestaurantUserDTO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TransportResponseBuilder {
    private final TransportRequestContact requestContact;
    private final TransportMainCommandProccessor mainCommandProccessor;
    private final TransportMainStepProccessor mainStepProcessor;
    private final TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility;

    public TransportResponseBuilder(TransportRequestContact requestContact, TransportMainCommandProccessor mainCommandProccessor, TransportMainStepProccessor mainStepProcessor, TelegramRestaurantUserDbUtility telegramRestaurantUserDbUtility) {
        this.requestContact = requestContact;
        this.mainCommandProccessor = mainCommandProccessor;
        this.mainStepProcessor = mainStepProcessor;
        this.telegramRestaurantUserDbUtility = telegramRestaurantUserDbUtility;
    }

    public void getResponse(Update update) {
        TelegramRestaurantUserDTO telegramUser = telegramRestaurantUserDbUtility.getTelegramUser(update);
        if (telegramUser == null) {
            telegramRestaurantUserDbUtility.registerTelegramUser(update);
            requestContact.requestContact(update);
        } else if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                mainCommandProccessor.commandProccessor(update, telegramUser);
            } else {
                mainStepProcessor.mainStepProcessor(update, telegramUser);
            }
        } else {
            mainStepProcessor.mainStepProcessor(update, telegramUser);
        }
    }
}
