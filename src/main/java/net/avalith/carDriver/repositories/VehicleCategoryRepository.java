package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.VehicleCategory;
import net.avalith.carDriver.models.enums.VehicleCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory,Long> {
    Optional<VehicleCategory> findByName(VehicleCategoryEnum name);

    @Query(value = "select * from vehicle_categories where is_active = true", nativeQuery = true)
    List<VehicleCategory> getAllActive();

    @Modifying
    @Transactional
    @Query(value = "update vehicle_categories set is_active = false where name = ?1 and is_active = true ", nativeQuery = true)
    Integer delete(String name);
}
