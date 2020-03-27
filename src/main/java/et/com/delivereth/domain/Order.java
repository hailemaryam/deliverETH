package et.com.delivereth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longtude")
    private String longtude;

    @Lob
    @Column(name = "location_description")
    private String locationDescription;

    @Column(name = "total_price")
    private String totalPrice;

    @Column(name = "date")
    private Instant date;

    @Lob
    @Column(name = "additional_note")
    private String additionalNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderedFood> orderedFoods = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("orders")
    private TelegramUser telegramUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public Order latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtude() {
        return longtude;
    }

    public Order longtude(String longtude) {
        this.longtude = longtude;
        return this;
    }

    public void setLongtude(String longtude) {
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public Order totalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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
            ", latitude='" + getLatitude() + "'" +
            ", longtude='" + getLongtude() + "'" +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", totalPrice='" + getTotalPrice() + "'" +
            ", date='" + getDate() + "'" +
            ", additionalNote='" + getAdditionalNote() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            "}";
    }
}
