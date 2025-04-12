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
import java.util.stream.Collectors;

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
            imageUrl = imageUtil.convertToBase64(file);
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

        // Set the spice owner's email
        User spiceOwner = spice.getUser();
        if (spiceOwner == null) {
            throw new RuntimeException("Spice owner not found");
        }
        bid.setSpiceOwnerEmail(spiceOwner.getEmail()); // Store the spice owner's email

        for (int i = 0; i < 3; i++) { // Retry up to 3 times
            try {
                Bid savedBid = bidRepo.save(bid);
                BidDTO<String> stringBidDTO = modelMapper.map(savedBid, BidDTO.class);
                stringBidDTO.setImageURL(imageUrl);

                // Send email after successful bid save
                String emailContent = "Your spice has been bid. Check it out!";
                try {
                    logger.info("Attempting to send email to spice owner: {}", spiceOwner.getEmail());
                    emailService.sendEmail(spiceOwner.getEmail(), "Spice Bid", emailContent);
                    logger.info("Email sent successfully to {}", spiceOwner.getEmail());
                } catch (Exception e) {
                    logger.error("Failed to send email to {}: {}", spiceOwner.getEmail(), e.getMessage());
                }

                return stringBidDTO;
            } catch (ObjectOptimisticLockingFailureException e) {
                logger.error("Optimistic locking failure: {}", e.getMessage());
                if (i == 2) {
                    throw e; // Rethrow after 3 attempts
                }
            }
        }

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
    public BidDTO<String> update(UUID id, BidDTO bidDTO, MultipartFile file) {
        return null;

    }

    @Override
    public boolean deleteBidById(String id) {
        return false;
    }

    @Override
    public List<BidDTO<String>> getBidsByUserId(UUID userId) {
        List<Bid> bids = bidRepo.findByUserUid(userId);
        return bids.stream()
                .map(bid -> {
                    BidDTO<String> bidDTO = modelMapper.map(bid, BidDTO.class);
                    bidDTO.setUserId(bid.getUser().getUid());
                    bidDTO.setListingName(bid.getListing().getName());
                    bidDTO.setListingDescription(bid.getListing().getDescription());
                    bidDTO.setImageURL(imageUtil.getImage(bid.getListing().getImageURL()));
                    bidDTO.setSpiceOwnerEmail(bid.getSpiceOwnerEmail()); // Explicitly set spiceOwnerEmail
                    return bidDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public BidDTO getBidById(UUID bidId) {
        Bid bid = bidRepo.findById(bidId).orElse(null);
        if (bid != null) {
            BidDTO bidDTO = modelMapper.map(bid, BidDTO.class);
            bidDTO.setUserId(bid.getUser().getUid());
            bidDTO.setListingName(bid.getListing().getName());
            bidDTO.setListingDescription(bid.getListing().getDescription());
            bidDTO.setImageURL(imageUtil.getImage(bid.getListing().getImageURL()));
            bidDTO.setSpiceOwnerEmail(bid.getSpiceOwnerEmail()); // Ensure this is set
            return bidDTO;
        } else {
            return null;
        }
    }

    @Override
    public void delete(String bidid) {
        bidRepo.deleteById(UUID.fromString(bidid));
    }

    @Transactional
    public void deleteBidsBySpiceId(UUID spiceId) {
        bidRepo.deleteByListing_id(spiceId);
    }
}
