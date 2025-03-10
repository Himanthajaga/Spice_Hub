package lk.ijse.back_end.service;


import lk.ijse.back_end.dto.UserDTO;


public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);
}