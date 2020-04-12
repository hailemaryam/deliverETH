package et.com.delivereth.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A TelegramDeliveryUser.
 */
@Entity
@Table(name = "telegram_delivery_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TelegramDeliveryUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    
    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "conversation_meta_data")
    private String conversationMetaData;

    @Column(name = "loaded_page")
    private Integer loadedPage;

    @OneToMany(mappedBy = "telegramDeliveryUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public TelegramDeliveryUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public TelegramDeliveryUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public TelegramDeliveryUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatId() {
        return chatId;
    }

    public TelegramDeliveryUser chatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getPhone() {
        return phone;
    }

    public TelegramDeliveryUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConversationMetaData() {
        return conversationMetaData;
    }

    public TelegramDeliveryUser conversationMetaData(String conversationMetaData) {
        this.conversationMetaData = conversationMetaData;
        return this;
    }

    public void setConversationMetaData(String conversationMetaData) {
        this.conversationMetaData = conversationMetaData;
    }

    public Integer getLoadedPage() {
        return loadedPage;
    }

    public TelegramDeliveryUser loadedPage(Integer loadedPage) {
        this.loadedPage = loadedPage;
        return this;
    }

    public void setLoadedPage(Integer loadedPage) {
        this.loadedPage = loadedPage;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public TelegramDeliveryUser orders(Set<Order> orders) {
        this.orders = orders;
        return this;
    }

    public TelegramDeliveryUser addOrder(Order order) {
        this.orders.add(order);
        order.setTelegramDeliveryUser(this);
        return this;
    }

    public TelegramDeliveryUser removeOrder(Order order) {
        this.orders.remove(order);
        order.setTelegramDeliveryUser(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TelegramDeliveryUser)) {
            return false;
        }
        return id != null && id.equals(((TelegramDeliveryUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TelegramDeliveryUser{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", conversationMetaData='" + getConversationMetaData() + "'" +
            ", loadedPage=" + getLoadedPage() +
            "}";
    }
}
