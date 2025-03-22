package lk.ijse.back_end.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.back_end.dto.BidDTO;
import lk.ijse.back_end.entity.Bid;
import lk.ijse.back_end.entity.Spice;
import lk.ijse.back_end.entity.User;
import lk.ijse.back_end.enums.ImageType;
import lk.ijse.back_end.repository.BidRepo;
import lk.ijse.back_end.repository.SpiceRepo;
import lk.ijse.back_end.repository.UserRepository;
import lk.ijse.back_end.service.BidService;
import lk.ijse.back_end.service.EmailService;
import lk.ijse.back_end.utill.ImageUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class BidServiceImpl implements BidService {
    private static final Logger logger = LoggerFactory.getLogger(BidServiceImpl.class);
    @Autowired
    private BidRepo bidRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ImageUtil imageUtil;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SpiceRepo spiceRepo;
    @Autowired
    private EmailService emailService;
    @Override
    @Transactional
    public BidDTO<String> save(BidDTO bidDTO, MultipartFile file) {
        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            imageUrl = imageUtil.saveImage(ImageType.BID, file);
            logger.info("Image saved with URL: {}", imageUrl);
        } else if (bidDTO.getImageURL() != null) {
            imageUrl = (String) bidDTO.getImageURL();
        }

        Bid bid = modelMapper.map(bidDTO, Bid.class);
        bid.setImageURL(generateShortURL());

        UUID userId = bidDTO.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        bid.setUser(user);

        Spice spice = spiceRepo.findById(bidDTO.getListingId()).orElseThrow(() -> new RuntimeException("Spice not found"));
        bid.setListing(spice);

        for (int i = 0; i < 3; i++) { // Retry up to 3 times
            try {
                Bid savedBid = bidRepo.save(bid);
                BidDTO<String> stringBidDTO = modelMapper.map(savedBid, BidDTO.class);
                stringBidDTO.setImageURL(imageUrl);
                return stringBidDTO;
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.error("Optimistic locking failure: {}", e.getMessage());
                if (i == 2) {
                    throw e; // Rethrow after 3 attempts
                }
            }
        }
        User spiceOwner = spice.getUser();
        String emailContent = "Your spice has been bid. Check it out!";
        emailService.sendEmail(spiceOwner.getEmail(), "Spice Bid", emailContent);

        throw new RuntimeException("Failed to save bid after 3 attempts");
    }

    private String generateShortURL() {
        return "SPICE-" + UUID.randomUUID().toString();
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
