package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepo extends JpaRepository<Bid, Long> {
    Bid findBidById(Long Id);
    boolean existsById(Long Id);
    void deleteById(Long Id);
    List<Bid> findByUserUid(Long userUid);
    List<Bid> findByListingId(Long listingId);
}
