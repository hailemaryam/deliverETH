package et.com.delivereth.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.delivereth.domain.TelegramUser} entity.
 */
public class TelegramUserDTO implements Serializable {
    
    private Long id;

    private String firstName;

    private String lastName;

    
    private String userName;

    private String chatId;

    private String phone;

    private String conversationMetaData;

    private Long orderIdPaused;

    private Long orderedFoodIdPaused;

    private Integer loadedPage;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConversationMetaData() {
        return conversationMetaData;
    }

    public void setConversationMetaData(String conversationMetaData) {
        this.conversationMetaData = conversationMetaData;
    }

    public Long getOrderIdPaused() {
        return orderIdPaused;
    }

    public void setOrderIdPaused(Long orderIdPaused) {
        this.orderIdPaused = orderIdPaused;
    }

    public Long getOrderedFoodIdPaused() {
        return orderedFoodIdPaused;
    }

    public void setOrderedFoodIdPaused(Long orderedFoodIdPaused) {
        this.orderedFoodIdPaused = orderedFoodIdPaused;
    }

    public Integer getLoadedPage() {
        return loadedPage;
    }

    public void setLoadedPage(Integer loadedPage) {
        this.loadedPage = loadedPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelegramUserDTO telegramUserDTO = (TelegramUserDTO) o;
        if (telegramUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telegramUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TelegramUserDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", conversationMetaData='" + getConversationMetaData() + "'" +
            ", orderIdPaused=" + getOrderIdPaused() +
            ", orderedFoodIdPaused=" + getOrderedFoodIdPaused() +
            ", loadedPage=" + getLoadedPage() +
            "}";
    }
}
