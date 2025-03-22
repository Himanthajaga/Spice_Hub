package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.PasswordResetToken;
import lk.ijse.back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByUser(User user);
    Optional<PasswordResetToken> findByToken(String token);
}
