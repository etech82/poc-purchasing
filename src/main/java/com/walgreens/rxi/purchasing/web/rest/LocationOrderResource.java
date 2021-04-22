package com.walgreens.rxi.purchasing.web.rest;

import com.walgreens.rxi.purchasing.domain.LocationOrder;
import com.walgreens.rxi.purchasing.repository.LocationOrderRepository;
import com.walgreens.rxi.purchasing.service.LocationOrderService;
import com.walgreens.rxi.purchasing.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.walgreens.rxi.purchasing.domain.LocationOrder}.
 */
@RestController
@RequestMapping("/api")
public class LocationOrderResource {

    private final Logger log = LoggerFactory.getLogger(LocationOrderResource.class);

    private static final String ENTITY_NAME = "purchasingLocationOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationOrderService locationOrderService;

    private final LocationOrderRepository locationOrderRepository;

    public LocationOrderResource(LocationOrderService locationOrderService, LocationOrderRepository locationOrderRepository) {
        this.locationOrderService = locationOrderService;
        this.locationOrderRepository = locationOrderRepository;
    }

    /**
     * {@code POST  /location-orders} : Create a new locationOrder.
     *
     * @param locationOrder the locationOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationOrder, or with status {@code 400 (Bad Request)} if the locationOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-orders")
    public ResponseEntity<LocationOrder> createLocationOrder(@Valid @RequestBody LocationOrder locationOrder) throws URISyntaxException {
        log.debug("REST request to save LocationOrder : {}", locationOrder);
        if (locationOrder.getId() != null) {
            throw new BadRequestAlertException("A new locationOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationOrder result = locationOrderService.save(locationOrder);
        return ResponseEntity
            .created(new URI("/api/location-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-orders/:id} : Updates an existing locationOrder.
     *
     * @param id the id of the locationOrder to save.
     * @param locationOrder the locationOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationOrder,
     * or with status {@code 400 (Bad Request)} if the locationOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-orders/{id}")
    public ResponseEntity<LocationOrder> updateLocationOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocationOrder locationOrder
    ) throws URISyntaxException {
        log.debug("REST request to update LocationOrder : {}, {}", id, locationOrder);
        if (locationOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationOrder result = locationOrderService.save(locationOrder);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationOrder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-orders/:id} : Partial updates given fields of an existing locationOrder, field will ignore if it is null
     *
     * @param id the id of the locationOrder to save.
     * @param locationOrder the locationOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationOrder,
     * or with status {@code 400 (Bad Request)} if the locationOrder is not valid,
     * or with status {@code 404 (Not Found)} if the locationOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-orders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationOrder> partialUpdateLocationOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocationOrder locationOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationOrder partially : {}, {}", id, locationOrder);
        if (locationOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationOrder> result = locationOrderService.partialUpdate(locationOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /location-orders} : get all the locationOrders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationOrders in body.
     */
    @GetMapping("/location-orders")
    public List<LocationOrder> getAllLocationOrders() {
        log.debug("REST request to get all LocationOrders");
        return locationOrderService.findAll();
    }

    /**
     * {@code GET  /location-orders/:id} : get the "id" locationOrder.
     *
     * @param id the id of the locationOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-orders/{id}")
    public ResponseEntity<LocationOrder> getLocationOrder(@PathVariable Long id) {
        log.debug("REST request to get LocationOrder : {}", id);
        Optional<LocationOrder> locationOrder = locationOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationOrder);
    }

    /**
     * {@code DELETE  /location-orders/:id} : delete the "id" locationOrder.
     *
     * @param id the id of the locationOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-orders/{id}")
    public ResponseEntity<Void> deleteLocationOrder(@PathVariable Long id) {
        log.debug("REST request to delete LocationOrder : {}", id);
        locationOrderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
