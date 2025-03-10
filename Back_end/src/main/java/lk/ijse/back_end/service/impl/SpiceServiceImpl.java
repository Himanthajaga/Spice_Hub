package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.Spice;
import lk.ijse.back_end.repository.SpiceRepo;
import lk.ijse.back_end.service.SpiceService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SpiceServiceImpl implements SpiceService {
    @Autowired
    private SpiceRepo spiceRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(SpiceDTO spiceListeningDTO) {
    if (!spiceRepo.existsById(spiceListeningDTO.getId())){
        Spice spiceListening=modelMapper.map(spiceListeningDTO, Spice.class);
        spiceRepo.save(spiceListening);
    }
    throw new RuntimeException("Spice Listening Already Exist");
    }

    @Override
    public List<SpiceDTO> getAll() {
        return modelMapper.map(spiceRepo.findAll(),new TypeToken<List<SpiceDTO>>(){}.getType());
    }

    @Override
    public void delete(Long id) {
if (spiceRepo.existsById(id)){
    spiceRepo.deleteById(id);
}
throw new RuntimeException("Spice Listening Not Found");
    }

    @Override
    public void update(SpiceDTO spiceListeningDTO) {
if (spiceRepo.existsById(spiceListeningDTO.getId())){
    Spice spiceListening=modelMapper.map(spiceListeningDTO, Spice.class);
    spiceRepo.save(spiceListening);
    return;
}
throw new RuntimeException("Spice Listening Not Found");
    }
}
