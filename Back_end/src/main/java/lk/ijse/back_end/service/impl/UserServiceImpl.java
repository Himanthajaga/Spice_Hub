package lk.ijse.back_end.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.back_end.dto.UserDTO;
import lk.ijse.back_end.entity.PasswordResetToken;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.enums.ImageType;
import lk.ijse.back_end.repository.PasswordResetTokenRepository;
import lk.ijse.back_end.repository.UserRepository;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.utill.ImageUtil;
import org.hibernate.StaleObjectStateException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger logger = LoggerFactory.getLogger(SpiceServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUtil imageUtil;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    @Override
    @Transactional
    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getProfilePicture() != null) {
            userDTO.setProfilePicture(imageUtil.getImage(user.getProfilePicture()));
        }
        return userDTO;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        Optional<PasswordResetToken> existingToken = Optional.ofNullable(tokenRepository.findByUser(user));

        if (existingToken.isPresent()) {
            PasswordResetToken passwordResetToken = existingToken.get();
            passwordResetToken.setToken(token);
            tokenRepository.save(passwordResetToken);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            tokenRepository.save(passwordResetToken);
        }

        return token;
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Transactional
    @Override
    public UserDTO<String> saveUser(UserDTO userDTO, MultipartFile file) {
        String base64Image = imageUtil.saveImage(ImageType.USER, file);
        User user = modelMapper.map(userDTO, User.class);
        user.setProfilePicture(base64Image);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(userDTO.getRole());
        user.setActive(true); //
        try {
            User savedUser = userRepository.save(user);
            UserDTO<String> stringUserDTO = modelMapper.map(savedUser, UserDTO.class);
            stringUserDTO.setProfilePicture(imageUtil.getImage(base64Image));
            return stringUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user");
        }
    }

    @Override
    public List<UserDTO<String>> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDTO<String>> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO<String>>>() {
        }.getType());
        for (UserDTO<String> userDTO : userDTOS) {
            users.stream().filter(user ->
                            user.getUid().equals(userDTO.getUid()))
                    .findFirst()
                    .ifPresent(user -> userDTO.setProfilePicture(imageUtil.getImage(user.getProfilePicture())));
        }
        return userDTOS;
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        logger.info("Deleting user with id: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public UserDTO<String> updateUser(UUID id, UserDTO userDTO, MultipartFile file) {
        Optional<User> tempUser = userRepository.findById(id);
        if (tempUser.isPresent()) {
            User user = tempUser.get();
            if (!file.isEmpty()) {
                String imageName = imageUtil.updateImage(user.getProfilePicture(), ImageType.USER, file);
                user.setProfilePicture(imageName);
            }
            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }
            if (userDTO.getPassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
            }
            if (userDTO.getAddress() != null) {
                user.setAddress(userDTO.getAddress());
            }
            if (userDTO.getPhone() != null) {
                user.setPhone(userDTO.getPhone());
            }
            if (userDTO.getRole() != null) {
                user.setRole(userDTO.getRole());
            }
            try {
                userRepository.save(user);
                logger.info("User updated successfully");
                return modelMapper.map(user, UserDTO.class);
            } catch (StaleObjectStateException e) {
                logger.error("Failed to update user: {}", user, e);
                throw new RuntimeException("Failed to update user");
            }
        } else {
            logger.warn("User with id {} not found", id);
            throw new RuntimeException("User not found");
        }
    }

    public void sendPasswordResetLink(String email) {
    }

    public void resetPassword(String email, String newPassword) throws Exception {
        User user = userRepository.findByEmail(email);
        PasswordResetToken token = tokenRepository.findByUser(user);

        if (token != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(token);
        } else {
            throw new Exception("Invalid token");
        }
    }

    @Override
    public void saveUser(UserDTO user) {
        User newUser = modelMapper.map(user, User.class);
        userRepository.save(newUser);
    }

    @Override
    public void deactivateUser(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO<String>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO<String>> userDTOs = new ArrayList<>();
        for (User user : users) {
            if (!"ADMIN".equals(user.getRole())) {
                UserDTO<String> userDTO = modelMapper.map(user, UserDTO.class);
                userDTO.setActive(user.isActive()); // Ensure active status is set
                if (user.getProfilePicture() != null) {
                    userDTO.setProfilePicture(imageUtil.getImage(user.getProfilePicture()));
                }
                userDTOs.add(userDTO);
            }
        }
        return userDTOs;
    }

    @Override
    public void toggleUserStatus(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public User findById(UUID sellerId) {
        if (sellerId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        // Add logging to see the value of sellerId
        logger.info("Seller ID: {}", sellerId);
        return userRepository.findById(sellerId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public String createPasswordResetOTP(String email) {
        User user = userRepository.findByEmail(email);
        String otp = String.valueOf(new Random().nextInt(999999));
        PasswordResetToken token = tokenRepository.findByUser(user);

        if (token != null) {
            token.setToken(otp);
            token.setExpiryDate(LocalDateTime.now().plusMinutes(15)); // Set new expiry date
        } else {
            token = new PasswordResetToken(otp, user);
        }
        tokenRepository.save(token);
        return otp;
    }

    @Override
    public boolean verifyPasswordResetOTP(String email, String otp) {
        User user = userRepository.findByEmail(email);
        PasswordResetToken token = tokenRepository.findByUser(user);
        return token != null && token.getToken().equals(otp);
    }
}