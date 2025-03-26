package lk.ijse.back_end.service;

import lk.ijse.back_end.dto.BidDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BidService {
   BidDTO<String>save(BidDTO bidDTO, MultipartFile file);
    List<BidDTO<String>> getAll();
    void delete(UUID id);
    BidDTO<String> update(UUID id,BidDTO bidDTO,MultipartFile file);
    boolean deleteBidById(String id);
    List<BidDTO<String>> getBidsByUserId(UUID userId);

    BidDTO getBidById(UUID bidId);
    void delete(String bidId);

}
