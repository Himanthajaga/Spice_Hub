package lk.ijse.back_end.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class AuthDTO {
    @Email(message = "Invalid email")
    private String email;
    private String token;
}