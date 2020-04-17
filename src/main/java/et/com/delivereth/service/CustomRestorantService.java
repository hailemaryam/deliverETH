package et.com.delivereth.service;

import et.com.delivereth.service.dto.RestorantDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link et.com.delivereth.domain.Restorant}.
 */
public interface CustomRestorantService {

    /**
     * Get all the restorants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestorantDTO> findAllSorByProximity(Float latitude, Float longitue, Float distance, Pageable pageable);

}
