package lk.ijse.back_end.controller;

import jakarta.validation.Valid;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.service.impl.BidServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bids")
@CrossOrigin(origins = "http://localhost:63342")
public class BidController {
@Autowired
private BidServiceImpl bidService;
@PostMapping(path = "/save")
public ResponseUtil saveBid(@Valid @RequestBody BidDTO bidDTO) {
bidService.save(bidDTO);
return new ResponseUtil(201,"Bid saved successfully",null);
}
@GetMapping(path = "/get")
public ResponseUtil getAllBids(){
return new ResponseUtil(201,"Bid saved successfully", bidService.getAll());
}
@DeleteMapping(path = "/delete/{id}")
public ResponseUtil deleteBid(@PathVariable UUID id){
bidService.delete(id);
return new ResponseUtil(201,"Bid Deleted Successfully",null);
}
@PutMapping(path = "/update")
public ResponseUtil updateBid(@RequestBody BidDTO bidDTO){
bidService.update(bidDTO);
return new ResponseUtil(201,"Bid Updated Successfully",null);
}
}
