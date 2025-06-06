package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Spice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpiceRepo extends JpaRepository<Spice, UUID> {

    SpiceRepo findByUserName(String userName);

    boolean existsByUserName(String userName);

    int deleteByUserName(String userName);

    List<Spice> findByCategory(String category);
    List<Spice> findByUserUid(UUID userUid);
    void deleteByName(String name);
    Optional<Spice> findById(UUID id);
    boolean existsByName(String name);
}
