package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.delivereth.domain.OrderedFood} entity.
 */
public class OrderedFoodDTO implements Serializable {
    
    private Long id;

    private Integer quantity;

    @Lob
    private String additionalNote;


    private Long foodId;

    private String foodName;

    private Long orderId;

    private String orderDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderedFoodDTO orderedFoodDTO = (OrderedFoodDTO) o;
        if (orderedFoodDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderedFoodDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderedFoodDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", additionalNote='" + getAdditionalNote() + "'" +
            ", foodId=" + getFoodId() +
            ", foodName='" + getFoodName() + "'" +
            ", orderId=" + getOrderId() +
            ", orderDate='" + getOrderDate() + "'" +
            "}";
    }
}
