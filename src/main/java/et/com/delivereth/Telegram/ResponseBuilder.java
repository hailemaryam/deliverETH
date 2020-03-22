package et.com.delivereth.Telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class ResponseBuilder {
    private final RequestContact requestContact;
    private final RequestLocation requestLocation;
    public ResponseBuilder(
        RequestContact requestContact,
        RequestLocation requestLocation) {
        this.requestContact = requestContact;
        this.requestLocation = requestLocation;
    }
    public SendMessage getResponse(Message message) {
        return requestLocation.requestLocation(message);
    }
}
