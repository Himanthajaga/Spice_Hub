package lk.ijse.back_end.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.back_end.dto.CategoryDTO;
import lk.ijse.back_end.service.impl.CategoryServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "http://localhost:63342")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveCategory(@RequestPart("category") String categoryJson, @RequestPart("file") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = new ObjectMapper().readValue(categoryJson, CategoryDTO.class);
            CategoryDTO<String> savedCategory = categoryService.save(categoryDTO, file);
            return new ResponseUtil(201, "Category added successfully", savedCategory);
        } catch (Exception e) {
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @GetMapping(path = "/get")
    public ResponseUtil getAllCategories() {
        List<CategoryDTO<String>> categories = categoryService.getAll();
        return new ResponseUtil(200, "Categories retrieved successfully", categories);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return new ResponseUtil(201, "Category Deleted Successfully", null);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateCategory(@RequestPart("category") String categoryJson, @RequestPart("file") MultipartFile file) {
        try {
            CategoryDTO categoryDTO = new ObjectMapper().readValue(categoryJson, CategoryDTO.class);
            CategoryDTO<String> updatedCategory = categoryService.update(categoryDTO.getId(), categoryDTO, file);
            return new ResponseUtil(200, "Category updated successfully", updatedCategory);
        } catch (Exception e) {
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @GetMapping(path = "/getById")
    public ResponseUtil getCategoryById(@RequestParam UUID id) {
        try {
            CategoryDTO<String> category = categoryService.getById(id);
            return new ResponseUtil(200, "Category retrieved successfully", category);
        } catch (Exception e) {
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
}
