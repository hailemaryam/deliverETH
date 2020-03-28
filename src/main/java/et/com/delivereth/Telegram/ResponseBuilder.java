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
    private final RequestRestorantSelection requestRestorantSelection;
    private final RequestFoodList requestFoodList;
    private final RequestQuantity requestQuantity;

    public ResponseBuilder(RequestContact requestContact, RequestLocation requestLocation, InlineButtonTest inlineButtonTest, DbUtility dbUtility, RequestErrorResponder requestErrorResponder, RequestForOrder requestForOrder, RequestRestorantSelection requestRestorantSelection, RequestFoodList requestFoodList, RequestQuantity requestQuantity) {
        this.requestContact = requestContact;
        this.requestLocation = requestLocation;
        this.inlineButtonTest = inlineButtonTest;
        this.dbUtility = dbUtility;
        this.requestErrorResponder = requestErrorResponder;
        this.requestForOrder = requestForOrder;
        this.requestRestorantSelection = requestRestorantSelection;
        this.requestFoodList = requestFoodList;
        this.requestQuantity = requestQuantity;
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
//        requestForOrder.requestForOrder(update);
//        requestRestorantSelection.requestRestorantSelection(update);
//        requestFoodList.requestFoodList(update);
//        requestQuantity.requestQuantity(update);
        return requestLocation.requestLocation(update.getMessage());
    }
}
