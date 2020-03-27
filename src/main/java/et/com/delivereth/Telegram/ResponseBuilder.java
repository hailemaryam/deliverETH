package et.com.delivereth.Telegram;

import et.com.delivereth.Telegram.Requests.*;
import et.com.delivereth.domain.TelegramUser;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ResponseBuilder {
    private final RequestContact requestContact;
    private final RequestLocation requestLocation;
    private final InlineButtonTest inlineButtonTest;
    private final DbUtility dbUtility;
    private final RequestErrorResponder requestErrorResponder;
    private final RequestForOrder requestForOrder;
    public ResponseBuilder(
        RequestForOrder requestForOrder,
        RequestErrorResponder requestErrorResponder,
        DbUtility dbUtility,
        InlineButtonTest inlineButtonTest,
        RequestContact requestContact,
        RequestLocation requestLocation) {
        this.requestForOrder = requestForOrder;
        this.requestErrorResponder = requestErrorResponder;
        this.dbUtility = dbUtility;
        this.inlineButtonTest = inlineButtonTest;
        this.requestContact = requestContact;
        this.requestLocation = requestLocation;
    }
    public BotApiMethod<Message> getResponse(Update update) {
//        TelegramUser telegramUser = dbUtility.getTelegramUser(update);
//        if (telegramUser != null) {
//            if (update.getMessage().getContact() != null) {
//                return inlineButtonTest.requestLocation(update.getMessage());
//            } else if(telegramUser.getPhone() == null) {
//                return requestContact.requestContactAgain(update.getMessage());
//            } else {
//                return requestErrorResponder.userErrorResponseResponder(update.getMessage());
//            }
//        } else {
//            dbUtility.registerTelegramUser(update);
//            return requestContact.requestContact(update.getMessage());
//        }
        requestForOrder.requestForOrder(update);
        return requestLocation.requestLocation(update.getMessage());
    }
}
