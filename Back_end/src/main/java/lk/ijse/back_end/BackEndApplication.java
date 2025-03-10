package lk.ijse.back_end;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "lk.ijse.back_end")
@EnableJpaRepositories(basePackages = "lk.ijse.back_end.repository")
@EntityScan(basePackages = "lk.ijse.back_end.entity")
public class BackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }
@Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
