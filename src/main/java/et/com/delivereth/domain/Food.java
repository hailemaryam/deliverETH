package et.com.delivereth.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Food.
 */
@Entity
@Table(name = "food")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "icon_image")
    private byte[] iconImage;

    @Column(name = "icon_image_content_type")
    private String iconImageContentType;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "food")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderedFood> orderedFoods = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("foods")
    private Restorant restorant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Food name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Food description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIconImage() {
        return iconImage;
    }

    public Food iconImage(byte[] iconImage) {
        this.iconImage = iconImage;
        return this;
    }

    public void setIconImage(byte[] iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImageContentType() {
        return iconImageContentType;
    }

    public Food iconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
        return this;
    }

    public void setIconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
    }

    public Double getPrice() {
        return price;
    }

    public Food price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<OrderedFood> getOrderedFoods() {
        return orderedFoods;
    }

    public Food orderedFoods(Set<OrderedFood> orderedFoods) {
        this.orderedFoods = orderedFoods;
        return this;
    }

    public Food addOrderedFood(OrderedFood orderedFood) {
        this.orderedFoods.add(orderedFood);
        orderedFood.setFood(this);
        return this;
    }

    public Food removeOrderedFood(OrderedFood orderedFood) {
        this.orderedFoods.remove(orderedFood);
        orderedFood.setFood(null);
        return this;
    }

    public void setOrderedFoods(Set<OrderedFood> orderedFoods) {
        this.orderedFoods = orderedFoods;
    }

    public Restorant getRestorant() {
        return restorant;
    }

    public Food restorant(Restorant restorant) {
        this.restorant = restorant;
        return this;
    }

    public void setRestorant(Restorant restorant) {
        this.restorant = restorant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Food)) {
            return false;
        }
        return id != null && id.equals(((Food) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Food{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", iconImage='" + getIconImage() + "'" +
            ", iconImageContentType='" + getIconImageContentType() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
