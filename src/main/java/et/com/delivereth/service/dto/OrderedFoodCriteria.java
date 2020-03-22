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
 * Criteria class for the {@link et.com.delivereth.domain.OrderedFood} entity. This class is used
 * in {@link et.com.delivereth.web.rest.OrderedFoodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ordered-foods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderedFoodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private LongFilter foodId;

    private LongFilter orderId;

    public OrderedFoodCriteria() {
    }

    public OrderedFoodCriteria(OrderedFoodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.foodId = other.foodId == null ? null : other.foodId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
    }

    @Override
    public OrderedFoodCriteria copy() {
        return new OrderedFoodCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getFoodId() {
        return foodId;
    }

    public void setFoodId(LongFilter foodId) {
        this.foodId = foodId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderedFoodCriteria that = (OrderedFoodCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(foodId, that.foodId) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantity,
        foodId,
        orderId
        );
    }

    @Override
    public String toString() {
        return "OrderedFoodCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (foodId != null ? "foodId=" + foodId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
            "}";
    }

}
