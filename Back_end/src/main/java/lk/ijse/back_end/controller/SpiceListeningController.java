package lk.ijse.back_end.controller;

import lk.ijse.back_end.dto.SpiceDTO;
import lk.ijse.back_end.service.impl.SpiceServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spice")
@CrossOrigin(origins = "http://localhost:63342")
public class SpiceListeningController {
    @Autowired
    private SpiceServiceImpl spiceServiceImpl;
    @PostMapping(path = "/save")
    public ResponseUtil saveSpiceListening(@RequestBody SpiceDTO SpiceListeningDTO) {
        spiceServiceImpl.save(SpiceListeningDTO);
        return new ResponseUtil(201,"Spice Listening saved successfully",null);
    }
    @GetMapping(path = "/get")
    public ResponseUtil getAllSpiceListenings(){
        return new ResponseUtil(201,"Spice Listening saved successfully", spiceServiceImpl.getAll());
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteSpiceListening(@PathVariable Long id){
        spiceServiceImpl.delete(id);
        return new ResponseUtil(201,"Spice Listening Deleted Successfully",null);
    }
    @PutMapping(path = "/update")
    public ResponseUtil updateSpiceListening(@RequestBody SpiceDTO SpiceListeningDTO){
        spiceServiceImpl.update(SpiceListeningDTO);
        return new ResponseUtil(201,"Spice Listening Updated Successfully",null);
    }

}
