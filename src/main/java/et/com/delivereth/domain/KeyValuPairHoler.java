package et.com.delivereth.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A KeyValuPairHoler.
 */
@Entity
@Table(name = "key_valu_pair_holer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class KeyValuPairHoler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "value_string")
    private String valueString;

    @Column(name = "value_number")
    private Double valueNumber;

    @Lob
    @Column(name = "value_image")
    private byte[] valueImage;

    @Column(name = "value_image_content_type")
    private String valueImageContentType;

    @Lob
    @Column(name = "value_blob")
    private byte[] valueBlob;

    @Column(name = "value_blob_content_type")
    private String valueBlobContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public KeyValuPairHoler key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValueString() {
        return valueString;
    }

    public KeyValuPairHoler valueString(String valueString) {
        this.valueString = valueString;
        return this;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Double getValueNumber() {
        return valueNumber;
    }

    public KeyValuPairHoler valueNumber(Double valueNumber) {
        this.valueNumber = valueNumber;
        return this;
    }

    public void setValueNumber(Double valueNumber) {
        this.valueNumber = valueNumber;
    }

    public byte[] getValueImage() {
        return valueImage;
    }

    public KeyValuPairHoler valueImage(byte[] valueImage) {
        this.valueImage = valueImage;
        return this;
    }

    public void setValueImage(byte[] valueImage) {
        this.valueImage = valueImage;
    }

    public String getValueImageContentType() {
        return valueImageContentType;
    }

    public KeyValuPairHoler valueImageContentType(String valueImageContentType) {
        this.valueImageContentType = valueImageContentType;
        return this;
    }

    public void setValueImageContentType(String valueImageContentType) {
        this.valueImageContentType = valueImageContentType;
    }

    public byte[] getValueBlob() {
        return valueBlob;
    }

    public KeyValuPairHoler valueBlob(byte[] valueBlob) {
        this.valueBlob = valueBlob;
        return this;
    }

    public void setValueBlob(byte[] valueBlob) {
        this.valueBlob = valueBlob;
    }

    public String getValueBlobContentType() {
        return valueBlobContentType;
    }

    public KeyValuPairHoler valueBlobContentType(String valueBlobContentType) {
        this.valueBlobContentType = valueBlobContentType;
        return this;
    }

    public void setValueBlobContentType(String valueBlobContentType) {
        this.valueBlobContentType = valueBlobContentType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyValuPairHoler)) {
            return false;
        }
        return id != null && id.equals(((KeyValuPairHoler) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "KeyValuPairHoler{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", valueString='" + getValueString() + "'" +
            ", valueNumber=" + getValueNumber() +
            ", valueImage='" + getValueImage() + "'" +
            ", valueImageContentType='" + getValueImageContentType() + "'" +
            ", valueBlob='" + getValueBlob() + "'" +
            ", valueBlobContentType='" + getValueBlobContentType() + "'" +
            "}";
    }
}
