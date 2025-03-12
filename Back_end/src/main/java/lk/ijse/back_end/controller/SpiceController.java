package lk.ijse.back_end.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.service.impl.SpiceServiceImpl;
import lk.ijse.back_end.utill.AppUtil;
import lk.ijse.back_end.utill.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/spice")
@CrossOrigin(origins = "http://localhost:63342")
public class SpiceController {
    private  static final Logger log = LoggerFactory.getLogger(SpiceController.class);
    @Autowired
    private SpiceServiceImpl spiceService;
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveSpice(@RequestPart("spice") String spiceJson, @RequestPart("file") MultipartFile file) {
        try {
            SpiceDTO spiceDTO = new ObjectMapper().readValue(spiceJson, SpiceDTO.class);
            log.info("Received request to save spice: {}", spiceDTO.getName());
            spiceDTO.setImageURL(file.getOriginalFilename());
            SpiceDTO<String> savedSpice = spiceService.save(spiceDTO, file);
            log.info("Spice saved successfully: {}", spiceDTO.getName());
            return new ResponseUtil(201, "Spice saved successfully", savedSpice);
        } catch (Exception e) {
            log.error("Error saving spice", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @GetMapping(path = "/get")
    public ResponseUtil getAllSpiceListenings(){
        try {
            List<SpiceDTO<String>> spices = spiceService.getAll();
            return new ResponseUtil(201, "Spices retrieved successfully", spices);
        } catch (Exception e) {
            log.error("Error retrieving spices", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteSpiceListening(@PathVariable UUID id){
        spiceService.delete(id);
        return new ResponseUtil(201,"Spice Listening Deleted Successfully",null);
    }
    @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateSpiceListening(@RequestPart("spice") String spiceJson, @RequestPart("file") MultipartFile file) {
        try {
            SpiceDTO spiceDTO = new ObjectMapper().readValue(spiceJson, SpiceDTO.class);
            log.info("Received request to update spice: {}", spiceDTO.getName());
            spiceDTO.setImageURL(file.getOriginalFilename());
            SpiceDTO<String> updatedSpice = spiceService.update(spiceDTO.getId(), spiceDTO, file);
            log.info("Spice updated successfully: {}", spiceDTO.getName());
            return new ResponseUtil(201, "Spice updated successfully", updatedSpice);
        } catch (Exception e) {
            log.error("Error updating spice", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
}
