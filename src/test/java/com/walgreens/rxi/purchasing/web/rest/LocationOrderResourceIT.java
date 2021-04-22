package com.walgreens.rxi.purchasing.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.walgreens.rxi.purchasing.IntegrationTest;
import com.walgreens.rxi.purchasing.domain.LocationOrder;
import com.walgreens.rxi.purchasing.domain.enumeration.OrderStatus;
import com.walgreens.rxi.purchasing.repository.LocationOrderRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocationOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocationOrderResourceIT {

    private static final UUID DEFAULT_CODE = UUID.randomUUID();
    private static final UUID UPDATED_CODE = UUID.randomUUID();

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PENDING;

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/location-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationOrderRepository locationOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationOrderMockMvc;

    private LocationOrder locationOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationOrder createEntity(EntityManager em) {
        LocationOrder locationOrder = new LocationOrder()
            .code(DEFAULT_CODE)
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .invoiceId(DEFAULT_INVOICE_ID);
        return locationOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationOrder createUpdatedEntity(EntityManager em) {
        LocationOrder locationOrder = new LocationOrder()
            .code(UPDATED_CODE)
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .invoiceId(UPDATED_INVOICE_ID);
        return locationOrder;
    }

    @BeforeEach
    public void initTest() {
        locationOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationOrder() throws Exception {
        int databaseSizeBeforeCreate = locationOrderRepository.findAll().size();
        // Create the LocationOrder
        restLocationOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isCreated());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeCreate + 1);
        LocationOrder testLocationOrder = locationOrderList.get(locationOrderList.size() - 1);
        assertThat(testLocationOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLocationOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testLocationOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLocationOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
    }

    @Test
    @Transactional
    void createLocationOrderWithExistingId() throws Exception {
        // Create the LocationOrder with an existing ID
        locationOrder.setId(1L);

        int databaseSizeBeforeCreate = locationOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationOrderRepository.findAll().size();
        // set the field null
        locationOrder.setCode(null);

        // Create the LocationOrder, which fails.

        restLocationOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationOrderRepository.findAll().size();
        // set the field null
        locationOrder.setPlacedDate(null);

        // Create the LocationOrder, which fails.

        restLocationOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = locationOrderRepository.findAll().size();
        // set the field null
        locationOrder.setStatus(null);

        // Create the LocationOrder, which fails.

        restLocationOrderMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocationOrders() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        // Get all the locationOrderList
        restLocationOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].placedDate").value(hasItem(DEFAULT_PLACED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID.intValue())));
    }

    @Test
    @Transactional
    void getLocationOrder() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        // Get the locationOrder
        restLocationOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, locationOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationOrder.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.placedDate").value(DEFAULT_PLACED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLocationOrder() throws Exception {
        // Get the locationOrder
        restLocationOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationOrder() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();

        // Update the locationOrder
        LocationOrder updatedLocationOrder = locationOrderRepository.findById(locationOrder.getId()).get();
        // Disconnect from session so that the updates on updatedLocationOrder are not directly saved in db
        em.detach(updatedLocationOrder);
        updatedLocationOrder.code(UPDATED_CODE).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).invoiceId(UPDATED_INVOICE_ID);

        restLocationOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationOrder.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationOrder))
            )
            .andExpect(status().isOk());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
        LocationOrder testLocationOrder = locationOrderList.get(locationOrderList.size() - 1);
        assertThat(testLocationOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLocationOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testLocationOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocationOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
    }

    @Test
    @Transactional
    void putNonExistingLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationOrder.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationOrderWithPatch() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();

        // Update the locationOrder using partial update
        LocationOrder partialUpdatedLocationOrder = new LocationOrder();
        partialUpdatedLocationOrder.setId(locationOrder.getId());

        partialUpdatedLocationOrder.status(UPDATED_STATUS);

        restLocationOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationOrder))
            )
            .andExpect(status().isOk());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
        LocationOrder testLocationOrder = locationOrderList.get(locationOrderList.size() - 1);
        assertThat(testLocationOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLocationOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testLocationOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocationOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
    }

    @Test
    @Transactional
    void fullUpdateLocationOrderWithPatch() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();

        // Update the locationOrder using partial update
        LocationOrder partialUpdatedLocationOrder = new LocationOrder();
        partialUpdatedLocationOrder.setId(locationOrder.getId());

        partialUpdatedLocationOrder.code(UPDATED_CODE).placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).invoiceId(UPDATED_INVOICE_ID);

        restLocationOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationOrder))
            )
            .andExpect(status().isOk());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
        LocationOrder testLocationOrder = locationOrderList.get(locationOrderList.size() - 1);
        assertThat(testLocationOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLocationOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testLocationOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLocationOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationOrder() throws Exception {
        int databaseSizeBeforeUpdate = locationOrderRepository.findAll().size();
        locationOrder.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationOrder))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationOrder in the database
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocationOrder() throws Exception {
        // Initialize the database
        locationOrderRepository.saveAndFlush(locationOrder);

        int databaseSizeBeforeDelete = locationOrderRepository.findAll().size();

        // Delete the locationOrder
        restLocationOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationOrder.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationOrder> locationOrderList = locationOrderRepository.findAll();
        assertThat(locationOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
