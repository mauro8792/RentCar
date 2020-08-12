package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.RideLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideLogRepository extends JpaRepository<RideLog, Long> {

}
