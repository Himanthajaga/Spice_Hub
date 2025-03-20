package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.entity.Bid;
import lk.ijse.back_end.enums.ImageType;
import lk.ijse.back_end.repository.BidRepo;
import lk.ijse.back_end.service.BidService;
import lk.ijse.back_end.utill.ImageUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class BidServiceImpl implements BidService {
    private static final Logger logger = LoggerFactory.getLogger(SpiceServiceImpl.class);
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUtil imageUtil;
    
    @Override
    @Transactional
    public BidDTO<String> save(BidDTO bidDTO, MultipartFile file) {
        String base64Image = imageUtil.saveImage(ImageType.BID, file);
        logger.info("Base64 image: {}", base64Image);
        Bid bid = modelMapper.map(bidDTO, Bid.class);
        bid.setImageURL(base64Image);
        try {
            Bid savedBid = bidRepo.save(bid);
            BidDTO<String> stringBidDTO = modelMapper.map(savedBid, BidDTO.class);
            stringBidDTO.setImageURL(base64Image);
            return stringBidDTO;
        } catch (Exception e) {
            logger.error("Failed to save bid: {}", bid, e);
            throw new RuntimeException("Failed to save bid");
        }
    }
    @Override
    public List<BidDTO<String>> getAll() {
        List<Bid> bids = bidRepo.findAll();
        List<BidDTO<String>> bidDTOS = modelMapper.map(bids, new TypeToken<List<BidDTO<String>>>() {
        }.getType());
        for (BidDTO<String> bidDTO : bidDTOS) {
            bids.stream().filter(bid ->
                    bid.getId().equals(bidDTO.getId()))
                    .findFirst()
                    .ifPresent(bid -> bidDTO.setImageURL(imageUtil.getImage(bid.getImageURL())));
        }
        return bidDTOS;
    }
    

    @Override
    @Transactional
    public void delete(UUID id) {
        logger.info("Deleting bid with id: {}", id);
        if (bidRepo.existsById(id)) {
            bidRepo.deleteById(id);
            logger.info("Bid deleted successfully");
        } else {
            logger.warn("Bid with id {} not found", id);
            throw new RuntimeException("Bid Listing Not Found");
        }
    }

    @Override
    public BidDTO<String> update(UUID id,BidDTO bidDTO,MultipartFile file) {
        return null;

    }

    @Override
    public boolean deleteBidById(String id) {
        return false;
    }
}
