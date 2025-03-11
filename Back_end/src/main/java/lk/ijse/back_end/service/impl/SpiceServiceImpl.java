package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.Spice;
import lk.ijse.back_end.repository.SpiceRepo;
import lk.ijse.back_end.service.SpiceService;
import org.hibernate.StaleObjectStateException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class SpiceServiceImpl implements SpiceService {
    private static final Logger logger = LoggerFactory.getLogger(SpiceServiceImpl.class);

    @Autowired
    private SpiceRepo spiceRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void save(SpiceDTO spiceDTO) {
        try {
            logger.info("Saving spice: {}", spiceDTO);
            Spice spice = modelMapper.map(spiceDTO, Spice.class);
            spiceRepo.save(spice);
            logger.info("Spice saved successfully: {}", spice);
        } catch (StaleObjectStateException e) {
            logger.error("StaleObjectStateException: Entity was updated or deleted by another transaction", e);
            throw new RuntimeException("Entity was updated or deleted by another transaction", e);
        } catch (Exception e) {
            logger.error("Exception occurred while saving spice", e);
            throw new RuntimeException("Error saving spice", e);
        }
    }

    @Override
    public List<SpiceDTO> getAll() {
        logger.info("Fetching all spices");
        List<SpiceDTO> spices = modelMapper.map(spiceRepo.findAll(), new TypeToken<List<SpiceDTO>>(){}.getType());
        logger.info("Fetched spices: {}", spices);
        return spices;
    }

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
    public void update(SpiceDTO spiceDTO) {
        logger.info("Updating spice: {}", spiceDTO);
        if (spiceRepo.existsById(spiceDTO.getId())) {
            Spice spice = modelMapper.map(spiceDTO, Spice.class);
            spiceRepo.save(spice);
            logger.info("Spice updated successfully: {}", spice);
        } else {
            logger.warn("Spice with id {} not found", spiceDTO.getId());
            throw new RuntimeException("Spice Listing Not Found");
        }
    }
}