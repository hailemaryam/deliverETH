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

    @Column(name = "total_price")
    private String totalPrice;

    @Column(name = "date")
    private Instant date;

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
            ", totalPrice='" + getTotalPrice() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
