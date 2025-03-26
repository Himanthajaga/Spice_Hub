package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepo extends JpaRepository<Bid, UUID> {
    Bid findBidById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    List<Bid> findByUserUid(UUID userUid);
    List<Bid> findByListingId(UUID listingId);

    void deleteByListing_id(UUID listingId);
}
