package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.CategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDTO<String> save(CategoryDTO categoryDTO, MultipartFile file);
    List<CategoryDTO<String>> getAll();
    void delete(UUID id);
    CategoryDTO<String> update(UUID id, CategoryDTO categoryDTO, MultipartFile file);
    CategoryDTO<String> getById(UUID id);
}
