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
 * Criteria class for the {@link et.com.delivereth.domain.TelegramDeliveryUser} entity. This class is used
 * in {@link et.com.delivereth.web.rest.TelegramDeliveryUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /telegram-delivery-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TelegramDeliveryUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter userName;

    private IntegerFilter userId;

    private StringFilter chatId;

    private StringFilter phone;

    private StringFilter conversationMetaData;

    private IntegerFilter loadedPage;

    private BooleanFilter status;

    private DoubleFilter currentBalance;

    private FloatFilter currentLatitude;

    private FloatFilter currentLongitude;

    private LongFilter orderId;

    private LongFilter restorantId;

    public TelegramDeliveryUserCriteria() {
    }

    public TelegramDeliveryUserCriteria(TelegramDeliveryUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.chatId = other.chatId == null ? null : other.chatId.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.conversationMetaData = other.conversationMetaData == null ? null : other.conversationMetaData.copy();
        this.loadedPage = other.loadedPage == null ? null : other.loadedPage.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.currentBalance = other.currentBalance == null ? null : other.currentBalance.copy();
        this.currentLatitude = other.currentLatitude == null ? null : other.currentLatitude.copy();
        this.currentLongitude = other.currentLongitude == null ? null : other.currentLongitude.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.restorantId = other.restorantId == null ? null : other.restorantId.copy();
    }

    @Override
    public TelegramDeliveryUserCriteria copy() {
        return new TelegramDeliveryUserCriteria(this);
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

    public IntegerFilter getUserId() {
        return userId;
    }

    public void setUserId(IntegerFilter userId) {
        this.userId = userId;
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

    public IntegerFilter getLoadedPage() {
        return loadedPage;
    }

    public void setLoadedPage(IntegerFilter loadedPage) {
        this.loadedPage = loadedPage;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public DoubleFilter getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(DoubleFilter currentBalance) {
        this.currentBalance = currentBalance;
    }

    public FloatFilter getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(FloatFilter currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public FloatFilter getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(FloatFilter currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getRestorantId() {
        return restorantId;
    }

    public void setRestorantId(LongFilter restorantId) {
        this.restorantId = restorantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TelegramDeliveryUserCriteria that = (TelegramDeliveryUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(chatId, that.chatId) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(conversationMetaData, that.conversationMetaData) &&
            Objects.equals(loadedPage, that.loadedPage) &&
            Objects.equals(status, that.status) &&
            Objects.equals(currentBalance, that.currentBalance) &&
            Objects.equals(currentLatitude, that.currentLatitude) &&
            Objects.equals(currentLongitude, that.currentLongitude) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(restorantId, that.restorantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        userName,
        userId,
        chatId,
        phone,
        conversationMetaData,
        loadedPage,
        status,
        currentBalance,
        currentLatitude,
        currentLongitude,
        orderId,
        restorantId
        );
    }

    @Override
    public String toString() {
        return "TelegramDeliveryUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (chatId != null ? "chatId=" + chatId + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (conversationMetaData != null ? "conversationMetaData=" + conversationMetaData + ", " : "") +
                (loadedPage != null ? "loadedPage=" + loadedPage + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (currentBalance != null ? "currentBalance=" + currentBalance + ", " : "") +
                (currentLatitude != null ? "currentLatitude=" + currentLatitude + ", " : "") +
                (currentLongitude != null ? "currentLongitude=" + currentLongitude + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
                (restorantId != null ? "restorantId=" + restorantId + ", " : "") +
            "}";
    }

}
