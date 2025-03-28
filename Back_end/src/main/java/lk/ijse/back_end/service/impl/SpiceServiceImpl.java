// SpiceServiceImpl.java
package lk.ijse.back_end.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
@Autowired
private BidServiceImpl bidService;
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
        return null;
    }

    @Override
    public SpiceDTO<String> getById(UUID id) {
        // Trim and convert the UUID to lowercase
        String trimmedId = id.toString().trim().toLowerCase();
        UUID uuid;
        try {
            uuid = UUID.fromString(trimmedId);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid UUID format: {}", trimmedId);
            throw new RuntimeException("Invalid UUID format");
        }

        logger.info("Retrieving spice with id: {}", uuid);
        Spice spice = spiceRepo.findById(uuid).orElse(null);
        if (spice == null) {
            logger.error("Spice not found with id: {}", uuid);
            return null;
        }
        SpiceDTO<String> spiceDTO = modelMapper.map(spice, SpiceDTO.class);
        logger.info("SpiceDTO retrieved: {}", spiceDTO);
        return spiceDTO;

    }


    @Override
    public boolean deleteSpiceById(String id) {
        UUID spiceId = UUID.fromString(id);
        if (spiceRepo.existsById(spiceId)) {
            spiceRepo.deleteById(spiceId);
            return true;
        } else {
            return false;
        }
    }
    @Override
    @Transactional
    public void delete(String spiceId) {
        if (spiceId == null || spiceId.isEmpty()) {
            throw new IllegalArgumentException("Spice ID cannot be null or empty");
        }

        // Assuming spiceId is a UUID string
        UUID spiceUUID;
        try {
            spiceUUID = UUID.fromString(spiceId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Spice ID format", e);
        }

        // Fetch the spice entity
        Spice spice = spiceRepo.findById(spiceUUID)
                .orElseThrow(() -> new EntityNotFoundException("Spice not found"));

        // Check if the name is null
        if (spice.getName() == null) {
            throw new NullPointerException("Spice name is null");
        }

        // Proceed with the deletion
        spiceRepo.delete(spice);
    }
@Transactional
    @Override
    public boolean deleteSpiceByName(String name) {
    if (spiceRepo.existsByName(name)) {
        spiceRepo.deleteByName(name);
        return true;
    } else {
        return false;
    }
}
}