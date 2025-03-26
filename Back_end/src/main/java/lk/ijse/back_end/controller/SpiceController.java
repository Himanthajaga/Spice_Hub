package lk.ijse.back_end.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.service.impl.SpiceServiceImpl;
import lk.ijse.back_end.utill.AppUtil;
import lk.ijse.back_end.utill.JwtUtil;
import lk.ijse.back_end.utill.ResponseUtil;
import lk.ijse.back_end.utill.VarList;
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
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveSpice(@RequestPart("spice") String spiceJson, @RequestPart("file") MultipartFile file, HttpServletRequest request) {
        try {
            String userEmail = jwtUtil.extractUserEmailFromJwt(jwtUtil.extractAuthTokenFromHeader(request));
            log.info("Received userEmail: {}", userEmail);

            // Fetch the user entity using the email
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                log.error("User not found with email: {}", userEmail);
                return new ResponseUtil(404, "User not found", null);
            }

            log.info("Received spiceJson: {}", spiceJson);

            // Validate JSON payload
            if (spiceJson == null || spiceJson.isEmpty()) {
                log.error("Spice JSON is null or empty");
                return new ResponseUtil(400, "Spice JSON is required", null);
            }

            SpiceDTO spiceDTO = new ObjectMapper().readValue(spiceJson, SpiceDTO.class);
            log.info("Deserialized SpiceDTO: {}", spiceDTO);

            if (spiceDTO.getName() == null || spiceDTO.getName().isEmpty()) {
                log.error("Spice name is null or empty");
                return new ResponseUtil(400, "Spice name is required", null);
            }

            log.info("Received request to save spice: {}", spiceDTO.getName());
            spiceDTO.setSellerId(user.getUid());
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
    @GetMapping(path = "/getByUser")
    public ResponseUtil getSpicesByUser(@RequestParam UUID userId) {
        try {
            List<SpiceDTO<String>> spices = spiceService.getByUserId(userId);
            return new ResponseUtil(201, "User's spices retrieved successfully", spices);
        } catch (Exception e) {
            log.error("Error retrieving user's spices", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @DeleteMapping(path = "/delete")
    public ResponseUtil deleteSpice(@RequestParam String id) {
        try {
            boolean isDeleted = spiceService.deleteSpiceById(id);
            if (isDeleted) {
                return new ResponseUtil(200, "Spice deleted successfully", null);
            } else {
                return new ResponseUtil(404, "Spice not found", null);
            }
        } catch (Exception e) {
            log.error("Error deleting spice", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateSpiceListening(@RequestPart("spice") String spiceJson, @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        try {
            SpiceDTO spiceDTO = new ObjectMapper().readValue(spiceJson, SpiceDTO.class);
            log.info("Received request to update spice: {}", spiceDTO.getName());
            spiceDTO.setImageURL(AppUtil.toBase64(file));

            // Assuming the spiceDTO contains the spice ID
            UUID spiceId = spiceDTO.getId();
            SpiceDTO<String> res = spiceService.update(spiceId, spiceDTO, file);
            if (res.equals(VarList.Created)) {
                return new ResponseUtil(VarList.Created, "Spice Updated Successfully", null);
            } else if (res.equals(VarList.Not_Acceptable)) {
                return new ResponseUtil(VarList.Not_Acceptable, "Spice Name Already Used", null);
            } else {
                return new ResponseUtil(VarList.Bad_Gateway, "Error", null);
            }
        } catch (Exception e) {
            log.error("Error updating spice", e);
            return new ResponseUtil(VarList.Internal_Server_Error, e.getMessage(), null);
        }
    }
    @GetMapping(path = "/getById")
    public ResponseUtil getSpiceById(@RequestParam String id) {
        try {
            SpiceDTO<String> spice = spiceService.getById(id);
            if (spice == null) {
                log.error("Spice not found with id: {}", id);
                return new ResponseUtil(404, "Spice not found", null);
            }
            return new ResponseUtil(200, "Spice retrieved successfully", spice);
        } catch (Exception e) {
            log.error("Error retrieving spice", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
    @DeleteMapping(path = "/deleteByName")
    public ResponseUtil deleteSpiceByName(@RequestParam String name) {
        try {
            boolean isDeleted = spiceService.deleteSpiceByName(name);
            if (isDeleted) {
                return new ResponseUtil(200, "Spice deleted successfully", null);
            } else {
                return new ResponseUtil(404, "Spice not found", null);
            }
        } catch (Exception e) {
            log.error("Error deleting spice", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
}
