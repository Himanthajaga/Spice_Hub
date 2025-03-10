package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Spice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpiceRepo extends JpaRepository<Spice,Long> {

    SpiceRepo findByUserName(String userName);

    boolean existsByUserName(String userName);

    int deleteByUserName(String userName);

    List<Spice> findByCategory(String category);
    List<Spice> findByUserUid(Long userUid);
}
