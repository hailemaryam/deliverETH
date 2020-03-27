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
 * Criteria class for the {@link et.com.delivereth.domain.KeyValuPairHoler} entity. This class is used
 * in {@link et.com.delivereth.web.rest.KeyValuPairHolerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /key-valu-pair-holers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class KeyValuPairHolerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter valueString;

    private DoubleFilter valueNumber;

    public KeyValuPairHolerCriteria() {
    }

    public KeyValuPairHolerCriteria(KeyValuPairHolerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.valueString = other.valueString == null ? null : other.valueString.copy();
        this.valueNumber = other.valueNumber == null ? null : other.valueNumber.copy();
    }

    @Override
    public KeyValuPairHolerCriteria copy() {
        return new KeyValuPairHolerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getValueString() {
        return valueString;
    }

    public void setValueString(StringFilter valueString) {
        this.valueString = valueString;
    }

    public DoubleFilter getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(DoubleFilter valueNumber) {
        this.valueNumber = valueNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final KeyValuPairHolerCriteria that = (KeyValuPairHolerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(valueString, that.valueString) &&
            Objects.equals(valueNumber, that.valueNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        key,
        valueString,
        valueNumber
        );
    }

    @Override
    public String toString() {
        return "KeyValuPairHolerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
                (valueString != null ? "valueString=" + valueString + ", " : "") +
                (valueNumber != null ? "valueNumber=" + valueNumber + ", " : "") +
            "}";
    }

}
