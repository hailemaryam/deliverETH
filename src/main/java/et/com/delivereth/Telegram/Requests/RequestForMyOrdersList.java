package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.Telegram.DbUtility;
import et.com.delivereth.Telegram.TelegramHome;
import et.com.delivereth.Telegram.TelegramSender;
import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestForMyOrdersList {
    private final TelegramSender telegramSender;
    private static final Logger logger = LoggerFactory.getLogger(TelegramHome.class);
    private final DbUtility dbUtility;

    public RequestForMyOrdersList(TelegramSender telegramSender, DbUtility dbUtility) {
        this.telegramSender = telegramSender;
        this.dbUtility = dbUtility;
    }

    public void requestForMyOrdersList(Update update, TelegramUserDTO telegramUser) {
        Page<OrderDTO> orderList = dbUtility.getMyOrders(telegramUser);
        if (orderList.toList().size() >0) {
            sendTitle(update);
        }
        orderList.toList().forEach(orderDTO -> {
            sendMyOrders(orderDTO, update);
        });
        if (orderList.toList().size() == 0) {
            sendNoMoreItem(update);
        } else if (orderList.hasNext()) {
            sendLoadMoreButton(update);
        }
    }

    private void sendMyOrders(OrderDTO orderDTO, Update update) {
        SendMessage response = new SendMessage();
        response.setReplyMarkup(myOrderInlineKeyBoard(orderDTO));
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        List<OrderedFoodDTO> orderedFoodList = dbUtility.getOrderedFoods(orderDTO.getId());
        String invoice = "";

        if (orderedFoodList.size() > 0) {
            invoice = invoice +  "<strong>Restaurant Name: " +
                dbUtility.getRestorant(dbUtility.getFood(orderedFoodList.get(0).getFoodId()).getRestorantId()).getName() +
                "</strong>\n";
        }
        Double total = 0D;
        for (OrderedFoodDTO orderedFood : orderedFoodList) {
            FoodDTO food = dbUtility.getFood(orderedFood.getFoodId());
            invoice = invoice + (orderedFood.getFoodName() + " * " + orderedFood.getQuantity() + " = " + orderedFood.getQuantity() * food.getPrice() + "\n");
            total += orderedFood.getQuantity() * food.getPrice();
        }
        invoice = invoice + "<b>Total = " + total + "</b> \n";
        invoice = invoice + "<b>Status = " + orderDTO.getOrderStatus() + "</b>";
        response.setText(invoice);
        response.setParseMode("HTML");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    private void sendLoadMoreButton(Update update) {
        SendMessage response = new SendMessage();
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Load More").setCallbackData("loadMore"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("Want to list more orders.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    private void sendNoMoreItem(Update update) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("There are no more orders to load.");
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public void sendTitle(Update update) {
        SendMessage response = new SendMessage();
        if (update.hasMessage()) {
            response.setChatId(update.getMessage().getChatId());
        } else if (update.hasCallbackQuery()) {
            response.setChatId(update.getCallbackQuery().getMessage().getChatId());
        }
        response.setText("<b>Your Order List</b>");
        response.setParseMode("HTML");
        response.setReplyMarkup(orderKeyBoardMenu());
        try {
            telegramSender.execute(response);
        } catch (TelegramApiException e) {
            logger.error("Error Sending Message {}", response);
        }
    }

    public ReplyKeyboardMarkup orderKeyBoardMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton()
            .setText("New Order"));
        keyboardButtons1.add(new KeyboardButton()
            .setText("My Orders"));
        KeyboardRow keyboardButtons2 = new KeyboardRow();
//        keyboardButtons2.add(new KeyboardButton()
//            .setText("Help"));
//        keyboardButtons2.add(new KeyboardButton()
//            .setText("Setting"));
        keyboardRowList.add(keyboardButtons1);
//        keyboardRowList.add(keyboardButtons2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
    public InlineKeyboardMarkup myOrderInlineKeyBoard(OrderDTO orderDTO){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        if (orderDTO.getOrderStatus().equals(OrderStatus.CANCELED_BY_RESTAURANT) ||
            orderDTO.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            rowInline.add(new InlineKeyboardButton().setText("Remove").setCallbackData("R_" + orderDTO.getId()));
        }
        if (orderDTO.getOrderStatus().equals(OrderStatus.ACCEPTED_BY_RESTAURANT) ||
            orderDTO.getOrderStatus().equals(OrderStatus.ORDERED)) {
            rowInline.add(new InlineKeyboardButton().setText("Cancel").setCallbackData("C_" + orderDTO.getId()));
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
