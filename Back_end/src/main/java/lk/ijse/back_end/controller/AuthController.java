package lk.ijse.back_end.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lk.ijse.back_end.dto.AuthDTO;
import lk.ijse.back_end.dto.ResponseDTO;
import lk.ijse.back_end.dto.TokenDTO;
import lk.ijse.back_end.dto.UserDTO;
import lk.ijse.back_end.service.EmailService;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.service.impl.UserServiceImpl;
import lk.ijse.back_end.utill.JwtUtil;
import lk.ijse.back_end.utill.ResponseUtil;
import lk.ijse.back_end.utill.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
@Autowired
private UserService userService1;
@Autowired
private EmailService emailService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final ResponseDTO responseDTO;
    @PostMapping("/google")
    public ResponseUtil googleLogin(@RequestBody TokenDTO tokenDTO) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(tokenDTO.getToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();

                // Check if user exists in your system
                UserDTO user = userService.loadUserDetailsByUsername(email);
                if (user == null) {
                    // Register the user if not exists
                    user = new UserDTO();
                    user.setEmail(email);
                    user.setName((String) payload.get("name"));
                    user.setProfilePicture((String) payload.get("picture"));
                    user.setPassword("google");
                    user.setEmail(email);
                    user.setPhone("0000000000");
                    user.setAddress("Google user");
                    userService.saveUser(user);
                }

                // Generate JWT token
                String token = jwtUtil.generateToken(user);
                return new ResponseUtil(200, "Login successful", token);
            } else {
                return new ResponseUtil(401, "Invalid ID token", null);
            }
        } catch (GeneralSecurityException | IOException e) {
            return new ResponseUtil(500, "Token verification failed: " + e.getMessage(), null);
        }
    }
    //constructor injection
    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService, ResponseDTO responseDTO) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.responseDTO = responseDTO;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        String token = jwtUtil.generateToken(loadedUser);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);
        authDTO.setRole(loadedUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(VarList.Created, "Success", authDTO));
    }
    @PostMapping("/forgot-password")
    public ResponseUtil forgotPassword(@RequestParam String email) {
        try {
            String token = userService.createPasswordResetToken(email);
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);
        } catch (Exception e) {
            return new ResponseUtil(200, "Password reset link sent to your email", null);
    }
        return new ResponseUtil(500, "Failed to send password reset link", null);
    }

    @PostMapping("/reset-password")
    public ResponseUtil resetPassword(@RequestParam String token, @RequestParam String password) {
        try {
            userService.resetPassword(token, password);
            return new ResponseUtil(200, "Password reset successfully", null);
        } catch (Exception e) {
            return new ResponseUtil(500, "Failed to reset password", null);
        }

}
}