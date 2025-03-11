package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.BidSpiceListningDTO;
import lk.ijse.back_end.dto.OrderDTO;
import lk.ijse.back_end.repository.OrderRepo;
import lk.ijse.back_end.repository.SpiceRepo;
import lk.ijse.back_end.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private SpiceRepo spiceRepo;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger LOGGER = Logger.getLogger(OrderServiceImpl.class.getName());
    @Override
    public void saveOrder(OrderDTO orderDTO) {

    }

    @Override
    public OrderDTO getOrderById(String orderId) {
        return null;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return null;
    }

    @Override
    public boolean checkItemsInStock(List<BidSpiceListningDTO> bidSpiceListnings) {
        return false;
    }

    @Override
    public String getLastOrderId() {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public void updateOrder(OrderDTO orderDTO) {

    }
}
