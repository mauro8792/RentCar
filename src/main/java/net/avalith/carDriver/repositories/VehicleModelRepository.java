package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.VehicleModels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModels,Long> {

    @Query(value = "select * from vehicle_models where is_active = true and name = ?1", nativeQuery = true)
    Optional<VehicleModels> findByName(String name);

    @Query(value = "select * from vehicle_models where is_active = true", nativeQuery = true)
    List<VehicleModels> getAllActive();
}
