package lk.ijse.back_end.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.entity.Bid;
import lk.ijse.back_end.entity.Bid.BidStatus;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.service.UserService;
import lk.ijse.back_end.service.impl.BidServiceImpl;
import lk.ijse.back_end.utill.JwtUtil;
import lk.ijse.back_end.utill.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bids")
@CrossOrigin(origins = "http://localhost:63342")
public class BidController {private  static final Logger log = LoggerFactory.getLogger(SpiceController.class);

    @Autowired
    private BidServiceImpl bidService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseUtil saveBid(@RequestBody @Valid String bidJson, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        try {
            String userEmail = jwtUtil.extractUserEmailFromJwt(jwtUtil.extractAuthTokenFromHeader(request));
            log.info("Received userEmail: {}", userEmail);

//            Fetch the user entity using the email
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                log.error("User not found with email: {}", userEmail);
                return new ResponseUtil(404, "User not found", null);
            }

            log.info("Received bidJson: {}", bidJson);

            // Validate JSON payload
            if (bidJson == null || bidJson.isEmpty()) {
                log.error("Bid JSON is null or empty");
                return new ResponseUtil(400, "Bid JSON is required", null);
            }
          BidDTO bidDTO = new ObjectMapper().readValue(bidJson, BidDTO.class);
            log.info("Deserialized BidDTO: {}", bidDTO);
            bidDTO.setStatus(String.valueOf(BidStatus.PENDING));
            bidDTO.setUserId(user.getUid());
            bidService.save(bidDTO, null);
            return new ResponseUtil(201, "Bid saved successfully", null);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
}