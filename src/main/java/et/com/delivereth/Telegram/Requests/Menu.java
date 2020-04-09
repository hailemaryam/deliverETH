package et.com.delivereth.Telegram.Requests;

import et.com.delivereth.domain.enumeration.OrderStatus;
import et.com.delivereth.service.dto.OrderDTO;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    public static InlineKeyboardMarkup finishOrAddMore(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Add More Item").setCallbackData("addMoreItem"));
        rowInline.add(new InlineKeyboardButton().setText("Order").setCallbackData("finishOrder"));
        rowInline.add(new InlineKeyboardButton().setText("Cancel").setCallbackData("cancelOrder"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    public static InlineKeyboardMarkup myOrderInlineKeyBoard(OrderDTO orderDTO){
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
    public static InlineKeyboardMarkup loadMore(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Load More >>").setCallbackData("loadMore"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    public static ReplyKeyboardMarkup orderKeyBoardMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton()
            .setText("New Order"));
        keyboardButtons1.add(new KeyboardButton()
            .setText("My Orders"));
        KeyboardRow keyboardButtons2 = new KeyboardRow();
        keyboardButtons2.add(new KeyboardButton()
            .setText("Cancel Order"));
        keyboardButtons2.add(new KeyboardButton()
            .setText("Help"));
        keyboardRowList.add(keyboardButtons1);
        keyboardRowList.add(keyboardButtons2);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup prepareShareLocationReplyButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow shareLocationButton = new KeyboardRow();
        shareLocationButton.add(new KeyboardButton()
            .setText("Share Location")
            .setRequestLocation(true));
        keyboardRowList.add(shareLocationButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText("Cancel Order"));
        keyboardRowList.add(cancelButton);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup prepareShareContactReplyButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow shareContactButton = new KeyboardRow();
        shareContactButton.add(new KeyboardButton()
            .setText("Share Contact").setRequestContact(true));
        keyboardRowList.add(shareContactButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText("Cancel"));
        keyboardRowList.add(cancelButton);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }
}
