package lk.ijse.back_end.service;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.SpiceDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface SpiceService {
  SpiceDTO<String> save(SpiceDTO spiceDTO, MultipartFile file);
  List<SpiceDTO<String>> getAll();

  void delete(UUID id);
  SpiceDTO<String> update(UUID id, SpiceDTO spiceDTO, MultipartFile file);

    List<SpiceDTO<String>> getByUserId(UUID userId);

  SpiceDTO<String> getById(UUID id);

  boolean deleteSpiceById(String id);

    void delete(String spiceId);
  boolean deleteSpiceByName(String name);
}
