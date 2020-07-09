package trade_point.service;

import org.springframework.stereotype.Service;
import trade_point.entity.TradePoint;
import trade_point.payload.TradePointRequest;
import trade_point.repository.TradePointRepository;

@Service
public class TradePointService {
    private final TradePointRepository tradePointRepository;

    TradePointService(TradePointRepository tradePointRepository) {
        this.tradePointRepository = tradePointRepository;
    }

    public boolean create(TradePointRequest tradePoint) {
        if (tradePointRepository.existsByName(tradePoint.getName())) {
            return false;
        } else {
            TradePoint point = new TradePoint();

            point.setAddress(tradePoint.getAddress());
            point.setLatitude(tradePoint.getLatitude());
            point.setLongitude(tradePoint.getLongitude());
            point.setName(tradePoint.getName());
            point.setIsActive(true);

            tradePointRepository.save(point);
             return true;
        }
    }
}
