package et.com.delivereth.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.delivereth.domain.TelegramRestaurantUser} entity.
 */
public class TelegramRestaurantUserDTO implements Serializable {
    
    private Long id;

    private String firstName;

    private String lastName;

    
    private String userName;

    
    private Integer userId;

    private String chatId;

    private String phone;

    private String conversationMetaData;

    private Integer loadedPage;

    private Boolean status;

    private Double currentBalance;

    private Set<RestorantDTO> restorants = new HashSet<>();
    
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getLoadedPage() {
        return loadedPage;
    }

    public void setLoadedPage(Integer loadedPage) {
        this.loadedPage = loadedPage;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Set<RestorantDTO> getRestorants() {
        return restorants;
    }

    public void setRestorants(Set<RestorantDTO> restorants) {
        this.restorants = restorants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelegramRestaurantUserDTO telegramRestaurantUserDTO = (TelegramRestaurantUserDTO) o;
        if (telegramRestaurantUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telegramRestaurantUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TelegramRestaurantUserDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", userId=" + getUserId() +
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", conversationMetaData='" + getConversationMetaData() + "'" +
            ", loadedPage=" + getLoadedPage() +
            ", status='" + isStatus() + "'" +
            ", currentBalance=" + getCurrentBalance() +
            ", restorants='" + getRestorants() + "'" +
            "}";
    }
}
