package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link et.com.delivereth.domain.TelegramUser} entity.
 */
public class TelegramUserDTO implements Serializable {
    
    private Long id;

    private String userName;

    private String chatId;

    private String phone;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
            ", userName='" + getUserName() + "'" +
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
