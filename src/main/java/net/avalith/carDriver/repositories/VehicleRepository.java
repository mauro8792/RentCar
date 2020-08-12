package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle>findByDomain(String domain);

    @Modifying
    @Transactional
    @Query(value = "update vehicles set is_active = false where domain = ?1 and is_active = true ", nativeQuery = true)
    Integer delete(String domain);
}
