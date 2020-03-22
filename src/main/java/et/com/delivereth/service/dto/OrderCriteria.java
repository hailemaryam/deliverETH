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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link et.com.delivereth.domain.Order} entity. This class is used
 * in {@link et.com.delivereth.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter latitude;

    private StringFilter longtude;

    private StringFilter totalPrice;

    private InstantFilter date;

    private LongFilter orderedFoodId;

    private LongFilter telegramUserId;

    public OrderCriteria() {
    }

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longtude = other.longtude == null ? null : other.longtude.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.orderedFoodId = other.orderedFoodId == null ? null : other.orderedFoodId.copy();
        this.telegramUserId = other.telegramUserId == null ? null : other.telegramUserId.copy();
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(StringFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getLongtude() {
        return longtude;
    }

    public void setLongtude(StringFilter longtude) {
        this.longtude = longtude;
    }

    public StringFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(StringFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public InstantFilter getDate() {
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public LongFilter getOrderedFoodId() {
        return orderedFoodId;
    }

    public void setOrderedFoodId(LongFilter orderedFoodId) {
        this.orderedFoodId = orderedFoodId;
    }

    public LongFilter getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(LongFilter telegramUserId) {
        this.telegramUserId = telegramUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longtude, that.longtude) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(date, that.date) &&
            Objects.equals(orderedFoodId, that.orderedFoodId) &&
            Objects.equals(telegramUserId, that.telegramUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        latitude,
        longtude,
        totalPrice,
        date,
        orderedFoodId,
        telegramUserId
        );
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longtude != null ? "longtude=" + longtude + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (orderedFoodId != null ? "orderedFoodId=" + orderedFoodId + ", " : "") +
                (telegramUserId != null ? "telegramUserId=" + telegramUserId + ", " : "") +
            "}";
    }

}
