package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link et.com.delivereth.domain.TelegramUser} entity. This class is used
 * in {@link et.com.delivereth.web.rest.TelegramUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /telegram-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TelegramUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter userName;

    private StringFilter chatId;

    private StringFilter phone;

    private StringFilter conversationMetaData;

    private LongFilter orderIdPaused;

    private LongFilter orderedFoodIdPaused;

    private LongFilter orderId;

    public TelegramUserCriteria() {
    }

    public TelegramUserCriteria(TelegramUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.chatId = other.chatId == null ? null : other.chatId.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.conversationMetaData = other.conversationMetaData == null ? null : other.conversationMetaData.copy();
        this.orderIdPaused = other.orderIdPaused == null ? null : other.orderIdPaused.copy();
        this.orderedFoodIdPaused = other.orderedFoodIdPaused == null ? null : other.orderedFoodIdPaused.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
    }

    @Override
    public TelegramUserCriteria copy() {
        return new TelegramUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getChatId() {
        return chatId;
    }

    public void setChatId(StringFilter chatId) {
        this.chatId = chatId;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getConversationMetaData() {
        return conversationMetaData;
    }

    public void setConversationMetaData(StringFilter conversationMetaData) {
        this.conversationMetaData = conversationMetaData;
    }

    public LongFilter getOrderIdPaused() {
        return orderIdPaused;
    }

    public void setOrderIdPaused(LongFilter orderIdPaused) {
        this.orderIdPaused = orderIdPaused;
    }

    public LongFilter getOrderedFoodIdPaused() {
        return orderedFoodIdPaused;
    }

    public void setOrderedFoodIdPaused(LongFilter orderedFoodIdPaused) {
        this.orderedFoodIdPaused = orderedFoodIdPaused;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TelegramUserCriteria that = (TelegramUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(chatId, that.chatId) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(conversationMetaData, that.conversationMetaData) &&
            Objects.equals(orderIdPaused, that.orderIdPaused) &&
            Objects.equals(orderedFoodIdPaused, that.orderedFoodIdPaused) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        userName,
        chatId,
        phone,
        conversationMetaData,
        orderIdPaused,
        orderedFoodIdPaused,
        orderId
        );
    }

    @Override
    public String toString() {
        return "TelegramUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (chatId != null ? "chatId=" + chatId + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (conversationMetaData != null ? "conversationMetaData=" + conversationMetaData + ", " : "") +
                (orderIdPaused != null ? "orderIdPaused=" + orderIdPaused + ", " : "") +
                (orderedFoodIdPaused != null ? "orderedFoodIdPaused=" + orderedFoodIdPaused + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
