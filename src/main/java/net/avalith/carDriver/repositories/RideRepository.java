package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE rides SET state = 'IN_RIDE' WHERE id_ride = ?1 ;", nativeQuery = true)
    Ride startRide(Long id);

    @Query(value = "SELECT * FROM rides\n" +
            "WHERE id_vehicle = ?1 AND state != 'FINISHED' AND state != 'CANCELLED' ;", nativeQuery = true)
    Optional<List<Ride>> findRidesByVehicle(Long vehicleId);

     @Query(value = "SELECT * FROM rides WHERE state = 'IN_RIDE' ;", nativeQuery = true)
    List<Ride> findAllRidesInRide();
}
