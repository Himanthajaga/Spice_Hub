package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
    @Query("SELECT o.oid FROM Order o ORDER BY o.oid DESC")
    String findLastOrderId();
}
