package et.com.delivereth.Telegram;

import et.com.delivereth.domain.TelegramUser;
import et.com.delivereth.repository.TelegramUserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Service
public class ResponseBuilder {
    private final RequestContact requestContact;
    private final RequestLocation requestLocation;
    private final InlineButtonTest inlineButtonTest;
    private final DbUtility dbUtility;
    private final ErrorResponder errorResponder;
    public ResponseBuilder(
        ErrorResponder errorResponder,
        DbUtility dbUtility,
        InlineButtonTest inlineButtonTest,
        RequestContact requestContact,
        RequestLocation requestLocation) {
        this.errorResponder = errorResponder;
        this.dbUtility = dbUtility;
        this.inlineButtonTest = inlineButtonTest;
        this.requestContact = requestContact;
        this.requestLocation = requestLocation;
    }
    public BotApiMethod<Message> getResponse(Update update) {
        TelegramUser telegramUser = dbUtility.getTelegramUser(update);
        if (telegramUser != null) {
            if (update.getMessage().getContact() != null) {
                return inlineButtonTest.requestLocation(update.getMessage());
            } else if(telegramUser.getPhone() == null) {
                return requestContact.requestContactAgain(update.getMessage());
            } else {
                return errorResponder.userErrorResponseResponder(update.getMessage());
            }
        } else {
            dbUtility.registerTelegramUser(update);
            return requestContact.requestContact(update.getMessage());
        }
    }
}
