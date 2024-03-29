package et.com.delivereth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Restorant.
 */
@Entity
@Table(name = "restorant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restorant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Pattern(regexp = "^[-a-zA-Z0-9@\\.+_]+$")
    @Column(name = "user_name", unique = true)
    private String userName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "icon_image")
    private byte[] iconImage;

    @Column(name = "icon_image_content_type")
    private String iconImageContentType;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longtude")
    private Float longtude;

    @Column(name = "available_order_cap")
    private Integer availableOrderCap;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "tin_number")
    private String tinNumber;

    @Column(name = "vat_number")
    private String vatNumber;

    @OneToMany(mappedBy = "restorant")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Food> foods = new HashSet<>();

    @ManyToMany(mappedBy = "restorants")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<TelegramRestaurantUser> telegramRestaurantUsers = new HashSet<>();

    @ManyToMany(mappedBy = "restorants")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<TelegramDeliveryUser> telegramDeliveryUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Restorant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public Restorant userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public Restorant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIconImage() {
        return iconImage;
    }

    public Restorant iconImage(byte[] iconImage) {
        this.iconImage = iconImage;
        return this;
    }

    public void setIconImage(byte[] iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImageContentType() {
        return iconImageContentType;
    }

    public Restorant iconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
        return this;
    }

    public void setIconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Restorant latitude(Float latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongtude() {
        return longtude;
    }

    public Restorant longtude(Float longtude) {
        this.longtude = longtude;
        return this;
    }

    public void setLongtude(Float longtude) {
        this.longtude = longtude;
    }

    public Integer getAvailableOrderCap() {
        return availableOrderCap;
    }

    public Restorant availableOrderCap(Integer availableOrderCap) {
        this.availableOrderCap = availableOrderCap;
        return this;
    }

    public void setAvailableOrderCap(Integer availableOrderCap) {
        this.availableOrderCap = availableOrderCap;
    }

    public Boolean isStatus() {
        return status;
    }

    public Restorant status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public Restorant tinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
        return this;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public Restorant vatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
        return this;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public Restorant foods(Set<Food> foods) {
        this.foods = foods;
        return this;
    }

    public Restorant addFood(Food food) {
        this.foods.add(food);
        food.setRestorant(this);
        return this;
    }

    public Restorant removeFood(Food food) {
        this.foods.remove(food);
        food.setRestorant(null);
        return this;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }

    public Set<TelegramRestaurantUser> getTelegramRestaurantUsers() {
        return telegramRestaurantUsers;
    }

    public Restorant telegramRestaurantUsers(Set<TelegramRestaurantUser> telegramRestaurantUsers) {
        this.telegramRestaurantUsers = telegramRestaurantUsers;
        return this;
    }

    public Restorant addTelegramRestaurantUser(TelegramRestaurantUser telegramRestaurantUser) {
        this.telegramRestaurantUsers.add(telegramRestaurantUser);
        telegramRestaurantUser.getRestorants().add(this);
        return this;
    }

    public Restorant removeTelegramRestaurantUser(TelegramRestaurantUser telegramRestaurantUser) {
        this.telegramRestaurantUsers.remove(telegramRestaurantUser);
        telegramRestaurantUser.getRestorants().remove(this);
        return this;
    }

    public void setTelegramRestaurantUsers(Set<TelegramRestaurantUser> telegramRestaurantUsers) {
        this.telegramRestaurantUsers = telegramRestaurantUsers;
    }

    public Set<TelegramDeliveryUser> getTelegramDeliveryUsers() {
        return telegramDeliveryUsers;
    }

    public Restorant telegramDeliveryUsers(Set<TelegramDeliveryUser> telegramDeliveryUsers) {
        this.telegramDeliveryUsers = telegramDeliveryUsers;
        return this;
    }

    public Restorant addTelegramDeliveryUser(TelegramDeliveryUser telegramDeliveryUser) {
        this.telegramDeliveryUsers.add(telegramDeliveryUser);
        telegramDeliveryUser.getRestorants().add(this);
        return this;
    }

    public Restorant removeTelegramDeliveryUser(TelegramDeliveryUser telegramDeliveryUser) {
        this.telegramDeliveryUsers.remove(telegramDeliveryUser);
        telegramDeliveryUser.getRestorants().remove(this);
        return this;
    }

    public void setTelegramDeliveryUsers(Set<TelegramDeliveryUser> telegramDeliveryUsers) {
        this.telegramDeliveryUsers = telegramDeliveryUsers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restorant)) {
            return false;
        }
        return id != null && id.equals(((Restorant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Restorant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", userName='" + getUserName() + "'" +
            ", description='" + getDescription() + "'" +
            ", iconImage='" + getIconImage() + "'" +
            ", iconImageContentType='" + getIconImageContentType() + "'" +
            ", latitude=" + getLatitude() +
            ", longtude=" + getLongtude() +
            ", availableOrderCap=" + getAvailableOrderCap() +
            ", status='" + isStatus() + "'" +
            ", tinNumber='" + getTinNumber() + "'" +
            ", vatNumber='" + getVatNumber() + "'" +
            "}";
    }
}
