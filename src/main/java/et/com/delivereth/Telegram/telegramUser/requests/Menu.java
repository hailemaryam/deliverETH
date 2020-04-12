package et.com.delivereth.Telegram.telegramUser.requests;

import et.com.delivereth.Telegram.Constants.StaticText;
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
        rowInline.add(new InlineKeyboardButton().setText(StaticText.addMore).setCallbackData("addMoreItem"));
        rowInline.add(new InlineKeyboardButton().setText(StaticText.finishOrder).setCallbackData("finishOrder"));
        rowInline.add(new InlineKeyboardButton().setText(StaticText.cancel).setCallbackData("cancelOrder"));
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
            rowInline.add(new InlineKeyboardButton().setText(StaticText.removeOrder).setCallbackData("R_" + orderDTO.getId()));
        }
        if (orderDTO.getOrderStatus().equals(OrderStatus.ORDERED)) {
            rowInline.add(new InlineKeyboardButton().setText(StaticText.cancel).setCallbackData("C_" + orderDTO.getId()));
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
    public static ReplyKeyboardMarkup orderKeyBoardMenu(boolean showCancel) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton()
            .setText(StaticText.newOrder));
        keyboardButtons1.add(new KeyboardButton()
            .setText(StaticText.myOrders));
        KeyboardRow keyboardButtons2 = new KeyboardRow();
        if (showCancel) {
            keyboardButtons2.add(new KeyboardButton()
                .setText(StaticText.cancelOrder));
        }
        keyboardButtons2.add(new KeyboardButton()
            .setText(StaticText.help));
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
            .setText(StaticText.shareLocation)
            .setRequestLocation(true));
        keyboardRowList.add(shareLocationButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText(StaticText.cancelOrder));
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
            .setText(StaticText.shareContact).setRequestContact(true));
        keyboardRowList.add(shareContactButton);
        KeyboardRow cancelButton = new KeyboardRow();
        cancelButton.add(new KeyboardButton()
            .setText(StaticText.cancel));
        keyboardRowList.add(cancelButton);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return  replyKeyboardMarkup;
    }
}
