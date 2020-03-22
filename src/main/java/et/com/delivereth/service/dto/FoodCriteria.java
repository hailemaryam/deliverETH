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
 * Criteria class for the {@link et.com.delivereth.domain.Food} entity. This class is used
 * in {@link et.com.delivereth.web.rest.FoodResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /foods?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FoodCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter price;

    private LongFilter orderedFoodId;

    private LongFilter restorantId;

    public FoodCriteria() {
    }

    public FoodCriteria(FoodCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.orderedFoodId = other.orderedFoodId == null ? null : other.orderedFoodId.copy();
        this.restorantId = other.restorantId == null ? null : other.restorantId.copy();
    }

    @Override
    public FoodCriteria copy() {
        return new FoodCriteria(this);
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

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public LongFilter getOrderedFoodId() {
        return orderedFoodId;
    }

    public void setOrderedFoodId(LongFilter orderedFoodId) {
        this.orderedFoodId = orderedFoodId;
    }

    public LongFilter getRestorantId() {
        return restorantId;
    }

    public void setRestorantId(LongFilter restorantId) {
        this.restorantId = restorantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FoodCriteria that = (FoodCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(orderedFoodId, that.orderedFoodId) &&
            Objects.equals(restorantId, that.restorantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        price,
        orderedFoodId,
        restorantId
        );
    }

    @Override
    public String toString() {
        return "FoodCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (orderedFoodId != null ? "orderedFoodId=" + orderedFoodId + ", " : "") +
                (restorantId != null ? "restorantId=" + restorantId + ", " : "") +
            "}";
    }

}
