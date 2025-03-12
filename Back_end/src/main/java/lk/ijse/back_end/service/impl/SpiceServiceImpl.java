// SpiceServiceImpl.java
package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.Spice;
import lk.ijse.back_end.enums.ImageType;
import lk.ijse.back_end.repository.SpiceRepo;
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

    @Override
    @Transactional
    public SpiceDTO<String> save(SpiceDTO spiceDTO, MultipartFile file) {
        String base64Image = imageUtil.saveImage( ImageType.SPICE,file);
        logger.info("Base64 image: {}", base64Image);
        Spice spice = modelMapper.map(spiceDTO, Spice.class);
        spice.setImageURL(base64Image);
        try {
            Spice savedSpice = spiceRepo.save(spice);
            SpiceDTO<String> stringSpiceDTO = modelMapper.map(savedSpice, SpiceDTO.class);
            stringSpiceDTO.setImageURL(base64Image);
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
            String base64Image = imageUtil.getImage(spiceDTO.getImageURL());
            spiceDTO.setImageURL(base64Image);
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
}