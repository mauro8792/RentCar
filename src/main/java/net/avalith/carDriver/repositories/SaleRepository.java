package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale,Long> {

}
