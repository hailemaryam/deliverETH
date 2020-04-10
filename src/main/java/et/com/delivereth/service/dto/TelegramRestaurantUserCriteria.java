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
 * Criteria class for the {@link et.com.delivereth.domain.TelegramRestaurantUser} entity. This class is used
 * in {@link et.com.delivereth.web.rest.TelegramRestaurantUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /telegram-restaurant-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TelegramRestaurantUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter userName;

    private StringFilter chatId;

    private StringFilter phone;

    private StringFilter conversationMetaData;

    private IntegerFilter loadedPage;

    private LongFilter restorantId;

    public TelegramRestaurantUserCriteria() {
    }

    public TelegramRestaurantUserCriteria(TelegramRestaurantUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.chatId = other.chatId == null ? null : other.chatId.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.conversationMetaData = other.conversationMetaData == null ? null : other.conversationMetaData.copy();
        this.loadedPage = other.loadedPage == null ? null : other.loadedPage.copy();
        this.restorantId = other.restorantId == null ? null : other.restorantId.copy();
    }

    @Override
    public TelegramRestaurantUserCriteria copy() {
        return new TelegramRestaurantUserCriteria(this);
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

    public IntegerFilter getLoadedPage() {
        return loadedPage;
    }

    public void setLoadedPage(IntegerFilter loadedPage) {
        this.loadedPage = loadedPage;
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
        final TelegramRestaurantUserCriteria that = (TelegramRestaurantUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(chatId, that.chatId) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(conversationMetaData, that.conversationMetaData) &&
            Objects.equals(loadedPage, that.loadedPage) &&
            Objects.equals(restorantId, that.restorantId);
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
        loadedPage,
        restorantId
        );
    }

    @Override
    public String toString() {
        return "TelegramRestaurantUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (chatId != null ? "chatId=" + chatId + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (conversationMetaData != null ? "conversationMetaData=" + conversationMetaData + ", " : "") +
                (loadedPage != null ? "loadedPage=" + loadedPage + ", " : "") +
                (restorantId != null ? "restorantId=" + restorantId + ", " : "") +
            "}";
    }

}
