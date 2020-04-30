package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link et.com.delivereth.domain.Restorant} entity. This class is used
 * in {@link et.com.delivereth.web.rest.RestorantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restorants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestorantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter userName;

    private FloatFilter latitude;

    private FloatFilter longtude;

    private IntegerFilter availableOrderCap;

    private BooleanFilter status;

    private StringFilter tinNumber;

    private StringFilter vatNumber;

    private LongFilter foodId;

    private LongFilter telegramRestaurantUserId;

    private LongFilter telegramDeliveryUserId;

    public RestorantCriteria() {
    }

    public RestorantCriteria(RestorantCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longtude = other.longtude == null ? null : other.longtude.copy();
        this.availableOrderCap = other.availableOrderCap == null ? null : other.availableOrderCap.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tinNumber = other.tinNumber == null ? null : other.tinNumber.copy();
        this.vatNumber = other.vatNumber == null ? null : other.vatNumber.copy();
        this.foodId = other.foodId == null ? null : other.foodId.copy();
        this.telegramRestaurantUserId = other.telegramRestaurantUserId == null ? null : other.telegramRestaurantUserId.copy();
        this.telegramDeliveryUserId = other.telegramDeliveryUserId == null ? null : other.telegramDeliveryUserId.copy();
    }

    @Override
    public RestorantCriteria copy() {
        return new RestorantCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public FloatFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(FloatFilter latitude) {
        this.latitude = latitude;
    }

    public FloatFilter getLongtude() {
        return longtude;
    }

    public void setLongtude(FloatFilter longtude) {
        this.longtude = longtude;
    }

    public IntegerFilter getAvailableOrderCap() {
        return availableOrderCap;
    }

    public void setAvailableOrderCap(IntegerFilter availableOrderCap) {
        this.availableOrderCap = availableOrderCap;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public StringFilter getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(StringFilter tinNumber) {
        this.tinNumber = tinNumber;
    }

    public StringFilter getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(StringFilter vatNumber) {
        this.vatNumber = vatNumber;
    }

    public LongFilter getFoodId() {
        return foodId;
    }

    public void setFoodId(LongFilter foodId) {
        this.foodId = foodId;
    }

    public LongFilter getTelegramRestaurantUserId() {
        return telegramRestaurantUserId;
    }

    public void setTelegramRestaurantUserId(LongFilter telegramRestaurantUserId) {
        this.telegramRestaurantUserId = telegramRestaurantUserId;
    }

    public LongFilter getTelegramDeliveryUserId() {
        return telegramDeliveryUserId;
    }

    public void setTelegramDeliveryUserId(LongFilter telegramDeliveryUserId) {
        this.telegramDeliveryUserId = telegramDeliveryUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestorantCriteria that = (RestorantCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longtude, that.longtude) &&
            Objects.equals(availableOrderCap, that.availableOrderCap) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tinNumber, that.tinNumber) &&
            Objects.equals(vatNumber, that.vatNumber) &&
            Objects.equals(foodId, that.foodId) &&
            Objects.equals(telegramRestaurantUserId, that.telegramRestaurantUserId) &&
            Objects.equals(telegramDeliveryUserId, that.telegramDeliveryUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        userName,
        latitude,
        longtude,
        availableOrderCap,
        status,
        tinNumber,
        vatNumber,
        foodId,
        telegramRestaurantUserId,
        telegramDeliveryUserId
        );
    }

    @Override
    public String toString() {
        return "RestorantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longtude != null ? "longtude=" + longtude + ", " : "") +
                (availableOrderCap != null ? "availableOrderCap=" + availableOrderCap + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (tinNumber != null ? "tinNumber=" + tinNumber + ", " : "") +
                (vatNumber != null ? "vatNumber=" + vatNumber + ", " : "") +
                (foodId != null ? "foodId=" + foodId + ", " : "") +
                (telegramRestaurantUserId != null ? "telegramRestaurantUserId=" + telegramRestaurantUserId + ", " : "") +
                (telegramDeliveryUserId != null ? "telegramDeliveryUserId=" + telegramDeliveryUserId + ", " : "") +
            "}";
    }

}
