package lk.ijse.back_end.controller;

import jakarta.validation.Valid;
import lk.ijse.back_end.dto.AuthDTO;
import lk.ijse.back_end.dto.ResponseDTO;
import lk.ijse.back_end.dto.UserDTO;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.utill.JwtUtil;
import lk.ijse.back_end.utill.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Constructor injection
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<ResponseDTO> registerUser(@RequestPart("user") @Valid UserDTO userDTO, @RequestPart("file") MultipartFile file) {
        try {
            // Save the profile picture
            String fileName = saveProfilePicture(file);
            userDTO.setProfilePicture(fileName);

            // Save the user
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    private String saveProfilePicture(MultipartFile file) throws IOException {
        String desktopPath = System.getProperty("user.home") + "/Desktop/profile_pictures/";
        File directory = new File(desktopPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Path path = Paths.get(desktopPath + file.getOriginalFilename());
        Files.write(path, file.getBytes());
        return file.getOriginalFilename();
    }
}