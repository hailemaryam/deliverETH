package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.delivereth.domain.Restorant} entity.
 */
public class RestorantDTO implements Serializable {
    
    private Long id;

    private String name;

    @Lob
    private String description;

    @Lob
    private byte[] iconImage;

    private String iconImageContentType;
    private String latitude;

    private String longtude;

    
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtude() {
        return longtude;
    }

    public void setLongtude(String longtude) {
        this.longtude = longtude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RestorantDTO restorantDTO = (RestorantDTO) o;
        if (restorantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), restorantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RestorantDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", iconImage='" + getIconImage() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longtude='" + getLongtude() + "'" +
            "}";
    }
}
