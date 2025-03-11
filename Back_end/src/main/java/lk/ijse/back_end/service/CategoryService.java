package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    void save(CategoryDTO categoryDTO);
    List<CategoryDTO> getAll();
    void delete(String  id);
    void update(CategoryDTO categoryDTO);
}
