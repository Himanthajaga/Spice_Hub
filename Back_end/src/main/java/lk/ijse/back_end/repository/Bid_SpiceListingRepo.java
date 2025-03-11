package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Bid_SpiceListning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Bid_SpiceListingRepo extends JpaRepository<Bid_SpiceListning, String> {
}
