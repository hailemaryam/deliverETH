package et.com.delivereth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import et.com.delivereth.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longtude")
    private Float longtude;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "location_description")
    private String locationDescription;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "transportation_fee")
    private Float transportationFee;

    @Column(name = "date")
    private Instant date;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "additional_note")
    private String additionalNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "restaurant_payment_staus")
    private Boolean restaurantPaymentStaus;

    @Column(name = "transport_payment_status")
    private Boolean transportPaymentStatus;

    @Column(name = "telegram_user_payment_status")
    private Boolean telegramUserPaymentStatus;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderedFood> orderedFoods = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private TelegramUser telegramUser;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private TelegramDeliveryUser telegramDeliveryUser;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private TelegramRestaurantUser telegramRestaurantUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Order latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongtude() {
        return longtude;
    }

    public Order longtude(Float longtude) {
        this.longtude = longtude;
        return this;
    }

    public void setLongtude(Float longtude) {
        this.longtude = longtude;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public Order locationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
        return this;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public Order totalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getTransportationFee() {
        return transportationFee;
    }

    public Order transportationFee(Float transportationFee) {
        this.transportationFee = transportationFee;
        return this;
    }

    public void setTransportationFee(Float transportationFee) {
        this.transportationFee = transportationFee;
    }

    public Instant getDate() {
        return date;
    }

    public Order date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public Order additionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
        return this;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Boolean isRestaurantPaymentStaus() {
        return restaurantPaymentStaus;
    }

    public Order restaurantPaymentStaus(Boolean restaurantPaymentStaus) {
        this.restaurantPaymentStaus = restaurantPaymentStaus;
        return this;
    }

    public void setRestaurantPaymentStaus(Boolean restaurantPaymentStaus) {
        this.restaurantPaymentStaus = restaurantPaymentStaus;
    }

    public Boolean isTransportPaymentStatus() {
        return transportPaymentStatus;
    }

    public Order transportPaymentStatus(Boolean transportPaymentStatus) {
        this.transportPaymentStatus = transportPaymentStatus;
        return this;
    }

    public void setTransportPaymentStatus(Boolean transportPaymentStatus) {
        this.transportPaymentStatus = transportPaymentStatus;
    }

    public Boolean isTelegramUserPaymentStatus() {
        return telegramUserPaymentStatus;
    }

    public Order telegramUserPaymentStatus(Boolean telegramUserPaymentStatus) {
        this.telegramUserPaymentStatus = telegramUserPaymentStatus;
        return this;
    }

    public void setTelegramUserPaymentStatus(Boolean telegramUserPaymentStatus) {
        this.telegramUserPaymentStatus = telegramUserPaymentStatus;
    }

    public Set<OrderedFood> getOrderedFoods() {
        return orderedFoods;
    }

    public Order orderedFoods(Set<OrderedFood> orderedFoods) {
        this.orderedFoods = orderedFoods;
        return this;
    }

    public Order addOrderedFood(OrderedFood orderedFood) {
        this.orderedFoods.add(orderedFood);
        orderedFood.setOrder(this);
        return this;
    }

    public Order removeOrderedFood(OrderedFood orderedFood) {
        this.orderedFoods.remove(orderedFood);
        orderedFood.setOrder(null);
        return this;
    }

    public void setOrderedFoods(Set<OrderedFood> orderedFoods) {
        this.orderedFoods = orderedFoods;
    }

    public TelegramUser getTelegramUser() {
        return telegramUser;
    }

    public Order telegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
        return this;
    }

    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public TelegramDeliveryUser getTelegramDeliveryUser() {
        return telegramDeliveryUser;
    }

    public Order telegramDeliveryUser(TelegramDeliveryUser telegramDeliveryUser) {
        this.telegramDeliveryUser = telegramDeliveryUser;
        return this;
    }

    public void setTelegramDeliveryUser(TelegramDeliveryUser telegramDeliveryUser) {
        this.telegramDeliveryUser = telegramDeliveryUser;
    }

    public TelegramRestaurantUser getTelegramRestaurantUser() {
        return telegramRestaurantUser;
    }

    public Order telegramRestaurantUser(TelegramRestaurantUser telegramRestaurantUser) {
        this.telegramRestaurantUser = telegramRestaurantUser;
        return this;
    }

    public void setTelegramRestaurantUser(TelegramRestaurantUser telegramRestaurantUser) {
        this.telegramRestaurantUser = telegramRestaurantUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longtude=" + getLongtude() +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", transportationFee=" + getTransportationFee() +
            ", date='" + getDate() + "'" +
            ", additionalNote='" + getAdditionalNote() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", restaurantPaymentStaus='" + isRestaurantPaymentStaus() + "'" +
            ", transportPaymentStatus='" + isTransportPaymentStatus() + "'" +
            ", telegramUserPaymentStatus='" + isTelegramUserPaymentStatus() + "'" +
            "}";
    }
}
