// SpiceServiceImpl.java
package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.Spice;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.enums.ImageType;
import lk.ijse.back_end.repository.SpiceRepo;
import lk.ijse.back_end.repository.UserRepository;
import lk.ijse.back_end.service.EmailService;
import lk.ijse.back_end.service.SpiceService;
import lk.ijse.back_end.utill.ImageUtil;
import org.hibernate.StaleObjectStateException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpiceServiceImpl implements SpiceService {
    private static final Logger logger = LoggerFactory.getLogger(SpiceServiceImpl.class);

    @Autowired
    private SpiceRepo spiceRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUtil imageUtil;
@Autowired
private UserRepository userRepo;
@Autowired
private EmailService emailService;
    @Override
    @Transactional
    public SpiceDTO<String> save(SpiceDTO spiceDTO, MultipartFile file) {
        String base64Image = imageUtil.saveImage(ImageType.SPICE, file);
        logger.info("Base64 image: {}", base64Image);
        Spice spice = modelMapper.map(spiceDTO, Spice.class);
        spice.setImageURL(base64Image);
        // Retrieve the User object using the userId from the SpiceDTO
        UUID userId = spiceDTO.getSellerId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        spice.setUser(user);

        try {
            Spice savedSpice = spiceRepo.save(spice);
            SpiceDTO<String> stringSpiceDTO = modelMapper.map(savedSpice, SpiceDTO.class);
            stringSpiceDTO.setImageURL(base64Image);

            List<User> users = userRepo.findAll();
            for (User registeredUser : users) {
                String emailContent = "A new spice has been added. Click the link to bid: <a href='http://localhost:8080/bidnow'>Bid Now</a>";
                emailService.sendEmail(registeredUser.getEmail(), "New Spice Added", emailContent);
            }

            return stringSpiceDTO;
        } catch (Exception e) {
            logger.error("Failed to save spice: {}", spice, e);
            throw new RuntimeException("Failed to save spice");
        }
    }


    @Override
    public List<SpiceDTO<String>> getAll() {
        List<Spice> spices = spiceRepo.findAll();
        List<SpiceDTO<String>> spiceDTOS = modelMapper.map(spices, new TypeToken<List<SpiceDTO<String>>>() {}.getType());
        for (SpiceDTO<String> spiceDTO : spiceDTOS) {
           spices.stream().filter(spice ->
                   spice.getId().equals(spiceDTO.getId()))
                   .findFirst()
                   .ifPresent(spice -> spiceDTO.setImageURL(imageUtil.getImage(spice.getImageURL())));
        }
        return spiceDTOS;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        logger.info("Deleting spice with id: {}", id);
        if (spiceRepo.existsById(id)) {
            spiceRepo.deleteById(id);
            logger.info("Spice deleted successfully");
        } else {
            logger.warn("Spice with id {} not found", id);
            throw new RuntimeException("Spice Listing Not Found");
        }
    }
@Transactional
    @Override
    public SpiceDTO<String> update(UUID id, SpiceDTO spiceDTO, MultipartFile file) {
        Optional<Spice> spice = spiceRepo.findById(id);
        if (spice.isPresent()) {
            String imageName = spice.get().getImageURL();
            if (!file.isEmpty()) {
                imageName = imageUtil.updateImage(spice.get().getImageURL(), ImageType.SPICE, file);
            }
            spice.get().setImageURL(imageName);
            spice.get().setName(spiceDTO.getName());
            spice.get().setPrice(spiceDTO.getPrice());
            spice.get().setQuantity(spiceDTO.getQuantity());
            spice.get().setDescription(spiceDTO.getDescription());
            spice.get().setCategory(spiceDTO.getCategory());
            try {
                spiceRepo.save(spice.get());
                logger.info("Spice updated successfully: {}", spice);
                return modelMapper.map(spice, SpiceDTO.class);
            } catch (StaleObjectStateException e) {
                logger.error("Failed to update spice: {}", spice, e);
                throw new RuntimeException("Failed to update spice");
            }
        } else {
            logger.warn("Spice with id {} not found", id);
            throw new RuntimeException("Spice Listing Not Found");
        }
    }

    @Override
    public List<SpiceDTO<String>> getByUserId(UUID userId) {
        List<Spice> spices = spiceRepo.findByUserUid(userId);
        List<SpiceDTO<String>> spiceDTOS = modelMapper.map(spices, new TypeToken<List<SpiceDTO<String>>>() {}.getType());
        for (SpiceDTO<String> spiceDTO : spiceDTOS) {
            spices.stream().filter(spice ->
                    spice.getId().equals(spiceDTO.getId()))
                    .findFirst()
                    .ifPresent(spice -> spiceDTO.setImageURL(imageUtil.getImage(spice.getImageURL())));
        }
        return spiceDTOS;
    }

    @Override
    public SpiceDTO<String> getById(String id) {
        Optional<Spice> spice = spiceRepo.findById(UUID.fromString(id));
        if (spice.isPresent()) {
            SpiceDTO<String> spiceDTO = modelMapper.map(spice.get(), SpiceDTO.class);
            spiceDTO.setImageURL(imageUtil.getImage(spice.get().getImageURL()));
            return spiceDTO;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteSpiceById(String id) {
        if (spiceRepo.existsById(UUID.fromString(id))) {
            spiceRepo.deleteById(UUID.fromString(id));
            return true;
        } else {
            return false;
        }
    }
}