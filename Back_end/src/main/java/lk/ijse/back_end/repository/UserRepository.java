package lk.ijse.back_end.repository;

import lk.ijse.back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

    int deleteByEmail(String userName);

}