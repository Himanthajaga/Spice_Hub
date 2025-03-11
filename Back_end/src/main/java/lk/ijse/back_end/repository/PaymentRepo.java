package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> {
    @Query("SELECT p.pid FROM Payment p ORDER BY p.pid DESC")
    String findLastPaymentId();
}
