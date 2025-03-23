package lk.ijse.back_end.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.entity.Bid.BidStatus;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.service.impl.BidServiceImpl;
import lk.ijse.back_end.utill.ImageUtil;
import lk.ijse.back_end.utill.JwtUtil;
import lk.ijse.back_end.utill.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bids")
@CrossOrigin(origins = "http://localhost:63342")
public class BidController {
    private  static final Logger log = LoggerFactory.getLogger(SpiceController.class);

    @Autowired
    private BidServiceImpl bidService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
@Autowired
private ObjectMapper objectMapper;
@Autowired
private ImageUtil imageUtil;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveBid(@RequestPart("bid") @Valid String bidJson, @RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "imageURL", required = false) String imageURL, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        try {
            String userEmail = jwtUtil.extractUserEmailFromJwt(token);
            log.info("Received userEmail: {}", userEmail);

            // Fetch the user entity using the email
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                log.error("User not found for email: {}", userEmail);
                return new ResponseUtil(404, "User not found", null);
            }

            log.info("Received bidJson: {}", bidJson);

            // Validate JSON payload
            if (bidJson == null || bidJson.isEmpty()) {
                log.error("Bid JSON is null or empty");
                return new ResponseUtil(400, "Bid JSON is required", null);
            }

            BidDTO bidDTO = objectMapper.readValue(bidJson, BidDTO.class);
            log.info("Deserialized BidDTO: {}", bidDTO);
            bidDTO.setStatus(BidStatus.PENDING.name());
            bidDTO.setUserId(user.getUid()); // Set the user ID

            if (file != null && !file.isEmpty()) {
                bidDTO.setImageURL(file.getOriginalFilename());
            } else if (imageURL != null && !imageURL.isEmpty()) {
                bidDTO.setImageURL(imageURL);
            }

            BidDTO<String> savedBid = bidService.save(bidDTO, file);
            log.info("Bid saved successfully: {}", savedBid);
            return new ResponseUtil(201, "Bid saved successfully", savedBid);
        } catch (JsonMappingException e) {
            log.error("Error mapping JSON", e);
            return new ResponseUtil(400, "Invalid JSON format", null);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON", e);
            return new ResponseUtil(500, "Internal server error", null);
        } catch (Exception e) {
            log.error("Unexpected error", e);
            return new ResponseUtil(500, "Internal server error", null);
        }
    }
            @GetMapping(path = "/get")
    public ResponseUtil getAllBids() {
        return new ResponseUtil(201, "Bid saved successfully", bidService.getAll());
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteBid(@PathVariable UUID id) {
        bidService.delete(id);
        return new ResponseUtil(201, "Bid Deleted Successfully", null);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil updateBid(@RequestPart("bid") String bidJson, @RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException {
        UUID bidId = UUID.fromString(bidJson);
        BidDTO bidDTO = new ObjectMapper().readValue(bidJson, BidDTO.class);
        bidService.update(bidId, bidDTO, file);
        return new ResponseUtil(201, "Bid Updated Successfully", null);
    }
    @GetMapping("/user")
    public ResponseUtil getBidsByUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtUtil.extractUserEmailFromJwt(token);
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            return new ResponseUtil(404, "User not found", null);
        }
        List<BidDTO<String>> bids = bidService.getBidsByUserId(user.getUid());
        return new ResponseUtil(200, "Bids retrieved successfully", bids);
    }
}