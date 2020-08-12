package net.avalith.carDriver.repositories;

import net.avalith.carDriver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where is_active = true and dni = ?1 ;", nativeQuery = true)
    Optional<User> getByDni(String dni);

    @Query(value = "select * from users where is_active = false and dni = ?1 ;", nativeQuery = true)
    Optional<User> getNotActiveByDni(String dni);

    @Query(value = "select * from users where is_active = true ", nativeQuery = true)
    List<User> getAll();

    @Modifying
    @Transactional
    @Query(value = "update users set is_active = false where dni = ?1 ;", nativeQuery = true)
    Integer delete(String dni);
}
