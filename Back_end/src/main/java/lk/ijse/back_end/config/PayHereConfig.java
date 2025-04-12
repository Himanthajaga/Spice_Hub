package lk.ijse.back_end.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
public class PayHereConfig {
    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

}
