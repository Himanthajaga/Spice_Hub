package lk.ijse.back_end.controller;

import lk.ijse.back_end.dto.OrderDTO;
import lk.ijse.back_end.service.impl.OrderServiceImpl;
import lk.ijse.back_end.utill.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@CrossOrigin(origins = "http://localhost:63342")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @PostMapping(path = "/save")
    public ResponseUtil saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.saveOrder(orderDTO);
        return new ResponseUtil(201,"Order saved successfully",null);
    }
    @GetMapping(path = "/get")
    public ResponseUtil getAllOrders(){
        return new ResponseUtil(201,"Order saved successfully", orderService.getAllOrders());
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseUtil deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return new ResponseUtil(201,"Order Deleted Successfully",null);
    }
    @PutMapping(path = "/update")
    public ResponseUtil updateOrder(@RequestBody OrderDTO orderDTO){
        orderService.updateOrder(orderDTO);
        return new ResponseUtil(201,"Order Updated Successfully",null);
    }
}
