package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.delivereth.domain.Food} entity.
 */
public class FoodDTO implements Serializable {
    
    private Long id;

    private String name;

    @Lob
    private String description;

    @Lob
    private byte[] iconImage;

    private String iconImageContentType;
    private Double price;


    private Long restorantId;

    private String restorantName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIconImage() {
        return iconImage;
    }

    public void setIconImage(byte[] iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImageContentType() {
        return iconImageContentType;
    }

    public void setIconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getRestorantId() {
        return restorantId;
    }

    public void setRestorantId(Long restorantId) {
        this.restorantId = restorantId;
    }

    public String getRestorantName() {
        return restorantName;
    }

    public void setRestorantName(String restorantName) {
        this.restorantName = restorantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FoodDTO foodDTO = (FoodDTO) o;
        if (foodDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foodDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FoodDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", iconImage='" + getIconImage() + "'" +
            ", price=" + getPrice() +
            ", restorantId=" + getRestorantId() +
            ", restorantName='" + getRestorantName() + "'" +
            "}";
    }
}
