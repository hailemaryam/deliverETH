package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import et.com.delivereth.domain.enumeration.OrderStatus;
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
    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {
        }

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter latitude;

    private FloatFilter longtude;

    private FloatFilter totalPrice;

    private FloatFilter transportationFee;

    private InstantFilter date;

    private OrderStatusFilter orderStatus;

    private BooleanFilter restaurantPaymentStaus;

    private BooleanFilter transportPaymentStatus;

    private BooleanFilter telegramUserPaymentStatus;

    private LongFilter orderedFoodId;

    private LongFilter telegramUserId;

    private LongFilter telegramDeliveryUserId;

    private LongFilter telegramRestaurantUserId;

    public OrderCriteria() {
    }

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longtude = other.longtude == null ? null : other.longtude.copy();
        this.totalPrice = other.totalPrice == null ? null : other.totalPrice.copy();
        this.transportationFee = other.transportationFee == null ? null : other.transportationFee.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.orderStatus = other.orderStatus == null ? null : other.orderStatus.copy();
        this.restaurantPaymentStaus = other.restaurantPaymentStaus == null ? null : other.restaurantPaymentStaus.copy();
        this.transportPaymentStatus = other.transportPaymentStatus == null ? null : other.transportPaymentStatus.copy();
        this.telegramUserPaymentStatus = other.telegramUserPaymentStatus == null ? null : other.telegramUserPaymentStatus.copy();
        this.orderedFoodId = other.orderedFoodId == null ? null : other.orderedFoodId.copy();
        this.telegramUserId = other.telegramUserId == null ? null : other.telegramUserId.copy();
        this.telegramDeliveryUserId = other.telegramDeliveryUserId == null ? null : other.telegramDeliveryUserId.copy();
        this.telegramRestaurantUserId = other.telegramRestaurantUserId == null ? null : other.telegramRestaurantUserId.copy();
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

    public FloatFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(FloatFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public FloatFilter getTransportationFee() {
        return transportationFee;
    }

    public void setTransportationFee(FloatFilter transportationFee) {
        this.transportationFee = transportationFee;
    }

    public InstantFilter getDate() {
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public OrderStatusFilter getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatusFilter orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BooleanFilter getRestaurantPaymentStaus() {
        return restaurantPaymentStaus;
    }

    public void setRestaurantPaymentStaus(BooleanFilter restaurantPaymentStaus) {
        this.restaurantPaymentStaus = restaurantPaymentStaus;
    }

    public BooleanFilter getTransportPaymentStatus() {
        return transportPaymentStatus;
    }

    public void setTransportPaymentStatus(BooleanFilter transportPaymentStatus) {
        this.transportPaymentStatus = transportPaymentStatus;
    }

    public BooleanFilter getTelegramUserPaymentStatus() {
        return telegramUserPaymentStatus;
    }

    public void setTelegramUserPaymentStatus(BooleanFilter telegramUserPaymentStatus) {
        this.telegramUserPaymentStatus = telegramUserPaymentStatus;
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

    public LongFilter getTelegramDeliveryUserId() {
        return telegramDeliveryUserId;
    }

    public void setTelegramDeliveryUserId(LongFilter telegramDeliveryUserId) {
        this.telegramDeliveryUserId = telegramDeliveryUserId;
    }

    public LongFilter getTelegramRestaurantUserId() {
        return telegramRestaurantUserId;
    }

    public void setTelegramRestaurantUserId(LongFilter telegramRestaurantUserId) {
        this.telegramRestaurantUserId = telegramRestaurantUserId;
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
            Objects.equals(transportationFee, that.transportationFee) &&
            Objects.equals(date, that.date) &&
            Objects.equals(orderStatus, that.orderStatus) &&
            Objects.equals(restaurantPaymentStaus, that.restaurantPaymentStaus) &&
            Objects.equals(transportPaymentStatus, that.transportPaymentStatus) &&
            Objects.equals(telegramUserPaymentStatus, that.telegramUserPaymentStatus) &&
            Objects.equals(orderedFoodId, that.orderedFoodId) &&
            Objects.equals(telegramUserId, that.telegramUserId) &&
            Objects.equals(telegramDeliveryUserId, that.telegramDeliveryUserId) &&
            Objects.equals(telegramRestaurantUserId, that.telegramRestaurantUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        latitude,
        longtude,
        totalPrice,
        transportationFee,
        date,
        orderStatus,
        restaurantPaymentStaus,
        transportPaymentStatus,
        telegramUserPaymentStatus,
        orderedFoodId,
        telegramUserId,
        telegramDeliveryUserId,
        telegramRestaurantUserId
        );
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longtude != null ? "longtude=" + longtude + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (transportationFee != null ? "transportationFee=" + transportationFee + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (orderStatus != null ? "orderStatus=" + orderStatus + ", " : "") +
                (restaurantPaymentStaus != null ? "restaurantPaymentStaus=" + restaurantPaymentStaus + ", " : "") +
                (transportPaymentStatus != null ? "transportPaymentStatus=" + transportPaymentStatus + ", " : "") +
                (telegramUserPaymentStatus != null ? "telegramUserPaymentStatus=" + telegramUserPaymentStatus + ", " : "") +
                (orderedFoodId != null ? "orderedFoodId=" + orderedFoodId + ", " : "") +
                (telegramUserId != null ? "telegramUserId=" + telegramUserId + ", " : "") +
                (telegramDeliveryUserId != null ? "telegramDeliveryUserId=" + telegramDeliveryUserId + ", " : "") +
                (telegramRestaurantUserId != null ? "telegramRestaurantUserId=" + telegramRestaurantUserId + ", " : "") +
            "}";
    }

}
