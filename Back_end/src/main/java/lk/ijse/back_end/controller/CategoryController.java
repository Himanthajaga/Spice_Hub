package lk.ijse.back_end.controller;

import lk.ijse.back_end.dto.CategoryDTO;
import lk.ijse.back_end.service.impl.CategoryServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "http://localhost:63342")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryServiceI;
    @PostMapping(path = "/save")
    public ResponseUtil saveCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryServiceI.save(categoryDTO);
        return new ResponseUtil(201,"Category saved successfully",null);
    }
    @GetMapping(path = "/get")
    public ResponseUtil getAllCategories(){
        return new ResponseUtil(201,"Category saved successfully", categoryServiceI.getAll());
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteCategory(@PathVariable String id){
        categoryServiceI.delete(id);
        return new ResponseUtil(201,"Category Deleted Successfully",null);
    }
    @PutMapping(path = "/update")
    public ResponseUtil updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryServiceI.update(categoryDTO);
        return new ResponseUtil(201,"Category Updated Successfully",null);
    }
}
