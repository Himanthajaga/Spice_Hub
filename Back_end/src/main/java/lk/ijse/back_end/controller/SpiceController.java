package lk.ijse.back_end.controller;

import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.service.impl.SpiceServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/spice")
@CrossOrigin(origins = "http://localhost:63342")
public class SpiceController {
    @Autowired
    private SpiceServiceImpl spiceService;
    @PostMapping(path = "/save")
        public ResponseUtil saveSpiceListening(@RequestBody SpiceDTO spiceDTO) {
            try {
                spiceService.save(spiceDTO);
                return new ResponseUtil(201, "Spice Listening Updated Successfully", true);
            } catch (RuntimeException e) {
                return new ResponseUtil(500, "Error updating spice", false);
            }
        }
    @GetMapping(path = "/get")
    public ResponseUtil getAllSpiceListenings(){
        return new ResponseUtil(201,"Spice Listening saved successfully", spiceService.getAll());
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteSpiceListening(@PathVariable UUID id){
        spiceService.delete(id);
        return new ResponseUtil(201,"Spice Listening Deleted Successfully",null);
    }
    @PutMapping(path = "/update")
    public ResponseUtil updateSpiceListening(@RequestBody SpiceDTO SpiceListeningDTO){
        try {
            spiceService.update(SpiceListeningDTO);
            return new ResponseUtil(201, "Spice Listening Updated Successfully", true);
        } catch (RuntimeException e) {
            return new ResponseUtil(409, e.getMessage(), false);
        }
    }
}
