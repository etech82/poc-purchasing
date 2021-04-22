package com.walgreens.rxi.purchasing.repository;

import com.walgreens.rxi.purchasing.domain.LocationOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LocationOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationOrderRepository extends JpaRepository<LocationOrder, Long> {}
