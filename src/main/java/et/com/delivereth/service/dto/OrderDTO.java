package et.com.delivereth.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import et.com.delivereth.domain.enumeration.OrderStatus;

/**
 * A DTO for the {@link et.com.delivereth.domain.Order} entity.
 */
public class OrderDTO implements Serializable {
    
    private Long id;

    private Float latitude;

    private Float longtude;

    @Lob
    private String locationDescription;

    private Float totalPrice;

    private Float transportationFee;

    private Instant date;

    @Lob
    private String additionalNote;

    private OrderStatus orderStatus;


    private Long telegramUserId;

    private String telegramUserUserName;

    private Long telegramDeliveryUserId;

    private String telegramDeliveryUserUserName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongtude() {
        return longtude;
    }

    public void setLongtude(Float longtude) {
        this.longtude = longtude;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getTransportationFee() {
        return transportationFee;
    }

    public void setTransportationFee(Float transportationFee) {
        this.transportationFee = transportationFee;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getTelegramUserId() {
        return telegramUserId;
    }

    public void setTelegramUserId(Long telegramUserId) {
        this.telegramUserId = telegramUserId;
    }

    public String getTelegramUserUserName() {
        return telegramUserUserName;
    }

    public void setTelegramUserUserName(String telegramUserUserName) {
        this.telegramUserUserName = telegramUserUserName;
    }

    public Long getTelegramDeliveryUserId() {
        return telegramDeliveryUserId;
    }

    public void setTelegramDeliveryUserId(Long telegramDeliveryUserId) {
        this.telegramDeliveryUserId = telegramDeliveryUserId;
    }

    public String getTelegramDeliveryUserUserName() {
        return telegramDeliveryUserUserName;
    }

    public void setTelegramDeliveryUserUserName(String telegramDeliveryUserUserName) {
        this.telegramDeliveryUserUserName = telegramDeliveryUserUserName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (orderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longtude=" + getLongtude() +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", transportationFee=" + getTransportationFee() +
            ", date='" + getDate() + "'" +
            ", additionalNote='" + getAdditionalNote() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", telegramUserId=" + getTelegramUserId() +
            ", telegramUserUserName='" + getTelegramUserUserName() + "'" +
            ", telegramDeliveryUserId=" + getTelegramDeliveryUserId() +
            ", telegramDeliveryUserUserName='" + getTelegramDeliveryUserUserName() + "'" +
            "}";
    }
}
