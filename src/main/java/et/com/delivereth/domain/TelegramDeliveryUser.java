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

    
    @Column(name = "user_id", unique = true)
    private Integer userId;

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "conversation_meta_data")
    private String conversationMetaData;

    @Column(name = "loaded_page")
    private Integer loadedPage;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "current_balance")
    private Double currentBalance;

    @Column(name = "current_latitude")
    private Float currentLatitude;

    @Column(name = "current_longitude")
    private Float currentLongitude;

    @OneToMany(mappedBy = "telegramDeliveryUser")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "telegram_delivery_user_restorant",
               joinColumns = @JoinColumn(name = "telegram_delivery_user_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "restorant_id", referencedColumnName = "id"))
    private Set<Restorant> restorants = new HashSet<>();

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

    public Integer getUserId() {
        return userId;
    }

    public TelegramDeliveryUser userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Boolean isStatus() {
        return status;
    }

    public TelegramDeliveryUser status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public TelegramDeliveryUser currentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Float getCurrentLatitude() {
        return currentLatitude;
    }

    public TelegramDeliveryUser currentLatitude(Float currentLatitude) {
        this.currentLatitude = currentLatitude;
        return this;
    }

    public void setCurrentLatitude(Float currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Float getCurrentLongitude() {
        return currentLongitude;
    }

    public TelegramDeliveryUser currentLongitude(Float currentLongitude) {
        this.currentLongitude = currentLongitude;
        return this;
    }

    public void setCurrentLongitude(Float currentLongitude) {
        this.currentLongitude = currentLongitude;
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

    public Set<Restorant> getRestorants() {
        return restorants;
    }

    public TelegramDeliveryUser restorants(Set<Restorant> restorants) {
        this.restorants = restorants;
        return this;
    }

    public TelegramDeliveryUser addRestorant(Restorant restorant) {
        this.restorants.add(restorant);
        restorant.getTelegramDeliveryUsers().add(this);
        return this;
    }

    public TelegramDeliveryUser removeRestorant(Restorant restorant) {
        this.restorants.remove(restorant);
        restorant.getTelegramDeliveryUsers().remove(this);
        return this;
    }

    public void setRestorants(Set<Restorant> restorants) {
        this.restorants = restorants;
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
            ", userId=" + getUserId() +
            ", chatId='" + getChatId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", conversationMetaData='" + getConversationMetaData() + "'" +
            ", loadedPage=" + getLoadedPage() +
            ", status='" + isStatus() + "'" +
            ", currentBalance=" + getCurrentBalance() +
            ", currentLatitude=" + getCurrentLatitude() +
            ", currentLongitude=" + getCurrentLongitude() +
            "}";
    }
}
