package lk.ijse.back_end.service;


import lk.ijse.back_end.dto.UserDTO;
import lk.ijse.back_end.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


public interface UserService {
    UserDTO<String>saveUser(UserDTO userDTO, MultipartFile file);
   List<UserDTO<String>>getAll();
    void deleteUser(UUID id);
    UserDTO<String>updateUser(UUID id, UserDTO userDTO, MultipartFile file);

    UserDTO loadUserDetailsByUsername(String email);
    User findByEmail(String email);

    String createPasswordResetToken(String email);
}