package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
   Optional<Brand> findByName(String name);

   @Query(value = "select * from brands where is_active = true", nativeQuery = true)
   List<Brand> getAllActive();
}
