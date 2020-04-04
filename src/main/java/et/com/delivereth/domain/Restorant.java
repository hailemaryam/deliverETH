package et.com.delivereth.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Restorant.
 */
@Entity
@Table(name = "restorant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restorant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "icon_image")
    private byte[] iconImage;

    @Column(name = "icon_image_content_type")
    private String iconImageContentType;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longtude")
    private String longtude;

    @OneToMany(mappedBy = "restorant")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Food> foods = new HashSet<>();

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

    public Restorant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Restorant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIconImage() {
        return iconImage;
    }

    public Restorant iconImage(byte[] iconImage) {
        this.iconImage = iconImage;
        return this;
    }

    public void setIconImage(byte[] iconImage) {
        this.iconImage = iconImage;
    }

    public String getIconImageContentType() {
        return iconImageContentType;
    }

    public Restorant iconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
        return this;
    }

    public void setIconImageContentType(String iconImageContentType) {
        this.iconImageContentType = iconImageContentType;
    }

    public String getLatitude() {
        return latitude;
    }

    public Restorant latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtude() {
        return longtude;
    }

    public Restorant longtude(String longtude) {
        this.longtude = longtude;
        return this;
    }

    public void setLongtude(String longtude) {
        this.longtude = longtude;
    }

    public Set<Food> getFoods() {
        return foods;
    }

    public Restorant foods(Set<Food> foods) {
        this.foods = foods;
        return this;
    }

    public Restorant addFood(Food food) {
        this.foods.add(food);
        food.setRestorant(this);
        return this;
    }

    public Restorant removeFood(Food food) {
        this.foods.remove(food);
        food.setRestorant(null);
        return this;
    }

    public void setFoods(Set<Food> foods) {
        this.foods = foods;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restorant)) {
            return false;
        }
        return id != null && id.equals(((Restorant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Restorant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", iconImage='" + getIconImage() + "'" +
            ", iconImageContentType='" + getIconImageContentType() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longtude='" + getLongtude() + "'" +
            "}";
    }
}
