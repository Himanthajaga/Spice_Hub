// CategoryServiceImpl.java
package lk.ijse.back_end.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.CategoryDTO;
import lk.ijse.back_end.entity.Category;
import lk.ijse.back_end.repository.CategoryRepo;
import lk.ijse.back_end.service.CategoryService;
import lk.ijse.back_end.utill.ImageUtil;
import lk.ijse.back_end.enums.ImageType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUtil imageUtil;

    @Override
    public CategoryDTO<String> save(CategoryDTO categoryDTO, MultipartFile file) {
        String base64Image = imageUtil.saveImage(ImageType.CATEGORY, file);
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setImageURL(base64Image);

        Category savedCategory = categoryRepo.save(category);
        CategoryDTO<String> stringCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        stringCategoryDTO.setImageURL(base64Image);

        return stringCategoryDTO;
    }

    @Override
    public List<CategoryDTO<String>> getAll() {
        List<Category> categories = categoryRepo.findAll();
        List<CategoryDTO<String>> categoryDTOS = modelMapper.map(categories, new TypeToken<List<CategoryDTO<String>>>() {
        }.getType());
        for (CategoryDTO<String> categoryDTO : categoryDTOS) {
            categories.stream().filter(category ->
                            category.getId().equals(categoryDTO.getId()))
                    .findFirst()
                    .ifPresent(category -> categoryDTO.setImageURL(imageUtil.getImage(category.getImageURL())));
        }
        return categoryDTOS;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
        } else {
            throw new EntityNotFoundException("Category not found");
        }
    }

    @Override
    @Transactional
    public CategoryDTO<String> update(UUID id, CategoryDTO categoryDTO, MultipartFile file) {
        Optional<Category> category = categoryRepo.findById(id);
        if (category.isPresent()) {
            String imageName = category.get().getImageURL();
            if (!file.isEmpty()) {
                imageName = imageUtil.updateImage(category.get().getImageURL(), ImageType.CATEGORY, file);
            }
            category.get().setImageURL(imageName);
            category.get().setName(categoryDTO.getName());
            category.get().setDescription(categoryDTO.getDescription());

            Category updatedCategory = categoryRepo.save(category.get());
            return modelMapper.map(updatedCategory, CategoryDTO.class);
        } else {
            throw new EntityNotFoundException("Category not found");
        }
    }

    @Override
    public CategoryDTO<String> getById(UUID id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        CategoryDTO<String> categoryDTO = modelMapper.map(category, CategoryDTO.class);
        categoryDTO.setImageURL(imageUtil.getImage(category.getImageURL())); // Ensure image is converted to base64
        return categoryDTO;
    }
}