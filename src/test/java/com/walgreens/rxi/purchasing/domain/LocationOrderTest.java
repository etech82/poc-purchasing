package com.walgreens.rxi.purchasing.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.walgreens.rxi.purchasing.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationOrder.class);
        LocationOrder locationOrder1 = new LocationOrder();
        locationOrder1.setId(1L);
        LocationOrder locationOrder2 = new LocationOrder();
        locationOrder2.setId(locationOrder1.getId());
        assertThat(locationOrder1).isEqualTo(locationOrder2);
        locationOrder2.setId(2L);
        assertThat(locationOrder1).isNotEqualTo(locationOrder2);
        locationOrder1.setId(null);
        assertThat(locationOrder1).isNotEqualTo(locationOrder2);
    }
}
