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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(SpiceServiceImpl.class);
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUtil imageUtil;

    @Override
    public CategoryDTO<String> save(CategoryDTO categoryDTO, MultipartFile file) {
        logger.info("Saving category: " + categoryDTO.getName());
        String base64Image = imageUtil.saveImage(ImageType.CATEGORY, file);
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setImageURL(base64Image);

        Category savedCategory = categoryRepo.save(category);
        logger.info("Category saved with ID: " + savedCategory.getId());

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
        Optional<Category> categoryOptional = categoryRepo.findById(id);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            if (categoryDTO.getName() != null) {
                category.setName(categoryDTO.getName());
            }
            if (categoryDTO.getDescription() != null) {
                category.setDescription(categoryDTO.getDescription());
            }
            if (file != null && !file.isEmpty()) {
                String imageName = imageUtil.updateImage(category.getImageURL(), ImageType.CATEGORY, file);
                category.setImageURL(imageName);
            }
            Category updatedCategory = categoryRepo.save(category);
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