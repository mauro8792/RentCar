package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Query(value = "select * from points where is_active = true and lat = ?1 and lng = ?2 ;", nativeQuery = true)
    Optional<Point> getByLatAndLng(String lat, String lng);

    @Query(value = "select * from points where is_active = true ", nativeQuery = true)
    List<Point> getAll();
}
