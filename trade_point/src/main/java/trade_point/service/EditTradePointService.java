package trade_point.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import trade_point.payload.EditSellerRequest;
import trade_point.entity.TradePoint;
import trade_point.entity.User;
import trade_point.exception.UserNotFoundException;
import trade_point.repository.TradePointRepository;
import trade_point.repository.UserRepository;

@Service
public class EditTradePointService {
    private static final ModelMapper mapper = new ModelMapper();

    private final Logger log = LoggerFactory.getLogger(EditTradePointService.class);

    private final UserRepository userRepository;

    private final TradePointRepository tradePointRepository;

    EditTradePointService(
            UserRepository userRepository,
            TradePointRepository tradePointRepository
    ) {
        this.tradePointRepository = tradePointRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Object> addSeller(EditSellerRequest editSellerRequest, TradePoint tradePoint)
            throws UserNotFoundException
    {
        if (!userRepository.existsById(editSellerRequest.getIdSeller())) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findById(editSellerRequest.getIdSeller()).get();

        user.getTradePoint().add(tradePoint);
        tradePoint.getUsers().add(user);

        userRepository.save(user);
        tradePointRepository.save(tradePoint);

        log.debug("Seller ({}) add to trade point ({})", user.getFirstName(), tradePoint.getName());

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }
}
