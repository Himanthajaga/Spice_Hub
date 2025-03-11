package lk.ijse.back_end.service.impl;

import lk.ijse.back_end.dto.CategoryDTO;
import lk.ijse.back_end.repository.CategoryRepo;
import lk.ijse.back_end.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(CategoryDTO categoryDTO) {

    }

    @Override
    public List<CategoryDTO> getAll() {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(CategoryDTO categoryDTO) {

    }
}
