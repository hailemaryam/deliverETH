package et.com.delivereth.repository;

import et.com.delivereth.domain.Restorant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Restorant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomRestaurantRepository extends JpaRepository<Restorant, Long>, JpaSpecificationExecutor<Restorant> {
    static final String HAVERSINE_PART = "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) * cos(radians(r.longtude) - radians(:longtude)) + sin(radians(:latitude)) * sin(radians(r.latitude))))";

    @Query(
        "SELECT r FROM Restorant r " +
            "WHERE " +
            HAVERSINE_PART + " < :distance AND r.status = true " +
            "ORDER BY " + HAVERSINE_PART + " ASC")
    public Page<Restorant> findEntitiesByLocation(
        @Param("latitude") final double latitude,
        @Param("longtude") final double longtude,
        @Param("distance") final double distance,
        Pageable pageable);
}
