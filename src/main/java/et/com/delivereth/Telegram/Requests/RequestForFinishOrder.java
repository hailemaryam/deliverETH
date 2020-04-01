package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.OrderedFood;
import et.com.delivereth.domain.TelegramUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestForFinishOrder {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(RequestForFinishOrder.class);
    private final DbUtility dbUtility;

    public RequestForFinishOrder(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }
    public void requestForFinishOrder(Update update, TelegramUser telegramUser) {
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Add More Item").setCallbackData("addMoreItem"));
        rowInline.add(new InlineKeyboardButton().setText("Order").setCallbackData("finishOrder"));
        rowInline.add(new InlineKeyboardButton().setText("Cancel").setCallbackData("cancelOrder"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        List<OrderedFood> orderedFoodList = dbUtility.getOrderedFoods(telegramUser);
        String invoice = "<strong>Order Confirmation</strong>\n";
        Double total = 0D;
        for (OrderedFood orderedFood : orderedFoodList) {
            invoice = invoice + (orderedFood.getFood().getName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * orderedFood.getFood().getPrice() + "\n" );
            total += orderedFood.getQuantity() * orderedFood.getFood().getPrice();
        }
        invoice = invoice + "<b>Total = " + total +"</b>";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void responseForFinishOrder(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Your order has been successfully registered.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
    public void responseForCancelOrder(Update update){
        SendMessage response = new SendMessage();
        if (update.hasMessage()){
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Your order has been successfully registered.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }
}
