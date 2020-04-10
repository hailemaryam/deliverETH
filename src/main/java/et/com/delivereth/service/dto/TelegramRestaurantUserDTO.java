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

    private String chatId;

    private String phone;

    private String conversationMetaData;

    private Integer loadedPage;

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
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", conversationMetaData='" + getConversationMetaData() + "'" +
            ", loadedPage=" + getLoadedPage() +
            ", restorants='" + getRestorants() + "'" +
            "}";
    }
}
