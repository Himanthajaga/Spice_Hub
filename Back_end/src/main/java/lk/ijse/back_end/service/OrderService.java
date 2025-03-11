package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.BidSpiceListningDTO;
import lk.ijse.back_end.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    void saveOrder(OrderDTO orderDTO);
    OrderDTO getOrderById(String orderId);

    List<OrderDTO> getAllOrders();

    boolean checkItemsInStock(List<BidSpiceListningDTO> bidSpiceListnings);
    String getLastOrderId();

    void deleteOrder(Long id);

    void updateOrder(OrderDTO orderDTO);
}
