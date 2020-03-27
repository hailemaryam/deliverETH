package et.com.delivereth.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link et.com.delivereth.domain.KeyValuPairHolder} entity.
 */
public class KeyValuPairHolderDTO implements Serializable {
    
    private Long id;

    private String key;

    private String valueString;

    private Double valueNumber;

    @Lob
    private byte[] valueImage;

    private String valueImageContentType;
    @Lob
    private byte[] valueBlob;

    private String valueBlobContentType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Double getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(Double valueNumber) {
        this.valueNumber = valueNumber;
    }

    public byte[] getValueImage() {
        return valueImage;
    }

    public void setValueImage(byte[] valueImage) {
        this.valueImage = valueImage;
    }

    public String getValueImageContentType() {
        return valueImageContentType;
    }

    public void setValueImageContentType(String valueImageContentType) {
        this.valueImageContentType = valueImageContentType;
    }

    public byte[] getValueBlob() {
        return valueBlob;
    }

    public void setValueBlob(byte[] valueBlob) {
        this.valueBlob = valueBlob;
    }

    public String getValueBlobContentType() {
        return valueBlobContentType;
    }

    public void setValueBlobContentType(String valueBlobContentType) {
        this.valueBlobContentType = valueBlobContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyValuPairHolderDTO keyValuPairHolderDTO = (KeyValuPairHolderDTO) o;
        if (keyValuPairHolderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), keyValuPairHolderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KeyValuPairHolderDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", valueNumber=" + getValueNumber() +
            ", valueImage='" + getValueImage() + "'" +
            ", valueBlob='" + getValueBlob() + "'" +
            "}";
    }
}
