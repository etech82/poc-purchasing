package com.walgreens.rxi.purchasing.service;

import com.walgreens.rxi.purchasing.domain.LocationOrder;
import com.walgreens.rxi.purchasing.repository.LocationOrderRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LocationOrder}.
 */
@Service
@Transactional
public class LocationOrderService {

    private final Logger log = LoggerFactory.getLogger(LocationOrderService.class);

    private final LocationOrderRepository locationOrderRepository;

    public LocationOrderService(LocationOrderRepository locationOrderRepository) {
        this.locationOrderRepository = locationOrderRepository;
    }

    /**
     * Save a locationOrder.
     *
     * @param locationOrder the entity to save.
     * @return the persisted entity.
     */
    public LocationOrder save(LocationOrder locationOrder) {
        log.debug("Request to save LocationOrder : {}", locationOrder);
        return locationOrderRepository.save(locationOrder);
    }

    /**
     * Partially update a locationOrder.
     *
     * @param locationOrder the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocationOrder> partialUpdate(LocationOrder locationOrder) {
        log.debug("Request to partially update LocationOrder : {}", locationOrder);

        return locationOrderRepository
            .findById(locationOrder.getId())
            .map(
                existingLocationOrder -> {
                    if (locationOrder.getCode() != null) {
                        existingLocationOrder.setCode(locationOrder.getCode());
                    }
                    if (locationOrder.getPlacedDate() != null) {
                        existingLocationOrder.setPlacedDate(locationOrder.getPlacedDate());
                    }
                    if (locationOrder.getStatus() != null) {
                        existingLocationOrder.setStatus(locationOrder.getStatus());
                    }
                    if (locationOrder.getInvoiceId() != null) {
                        existingLocationOrder.setInvoiceId(locationOrder.getInvoiceId());
                    }

                    return existingLocationOrder;
                }
            )
            .map(locationOrderRepository::save);
    }

    /**
     * Get all the locationOrders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocationOrder> findAll() {
        log.debug("Request to get all LocationOrders");
        return locationOrderRepository.findAll();
    }

    /**
     * Get one locationOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocationOrder> findOne(Long id) {
        log.debug("Request to get LocationOrder : {}", id);
        return locationOrderRepository.findById(id);
    }

    /**
     * Delete the locationOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationOrder : {}", id);
        locationOrderRepository.deleteById(id);
    }
}
